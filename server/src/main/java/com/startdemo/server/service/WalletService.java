package com.startdemo.server.service;

import com.startdemo.server.domain.DemoWallet;
import com.startdemo.server.domain.GameSession;
import com.startdemo.server.domain.TransactionType;
import com.startdemo.server.domain.WalletTransaction;
import com.startdemo.server.error.BusinessException;
import com.startdemo.server.repository.DemoWalletRepository;
import com.startdemo.server.repository.WalletTransactionRepository;
import com.startdemo.server.web.dto.BalanceResponse;
import com.startdemo.server.web.dto.TransactionResponse;
import com.startdemo.server.web.dto.WalletResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class WalletService {

    private static final int MONEY_SCALE = 2;
    private static final String MISMATCH = "交易号已存在，但请求内容不一致";

    private final DemoWalletRepository walletRepository;
    private final WalletTransactionRepository transactionRepository;
    private final GameSessionService sessionService;

    public WalletService(
            DemoWalletRepository walletRepository,
            WalletTransactionRepository transactionRepository,
            GameSessionService sessionService
    ) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.sessionService = sessionService;
    }

    public BalanceResponse getBalance(String uid) {
        return toBalance(requireWallet(uid));
    }

    public List<TransactionResponse> listTransactions(String uid) {
        requireWallet(uid);
        return transactionRepository.findByUidOrderByCreatedAtDesc(uid).stream()
                .map(TransactionResponse::from)
                .toList();
    }

    /**
     * DEMO ONLY.
     * 钱包不存在时，用前端初始余额创建 DemoWallet。
     * 钱包已存在时，只更新用户名，余额保持 Java 当前值，不能被前端余额覆盖。
     */
    @Transactional
    public BalanceResponse syncDemoBalance(String uid, String username, BigDecimal balance) {
        return walletRepository.findByUidForUpdate(uid)
                .map(wallet -> keepServerBalance(wallet, username))
                .orElseGet(() -> toBalance(walletRepository.save(new DemoWallet(uid, username, nonNegative(balance)))));
    }

    /**
     * DEMO ONLY.
     * 删除该用户的游戏会话和钱包流水，并把余额重置为 0。
     * 正式生产系统不应提供由调用方重置余额的接口。
     */
    @Transactional
    public BalanceResponse resetDemo(String uid) {
        DemoWallet wallet = requireWallet(uid);
        transactionRepository.deleteByUid(uid);
        sessionService.deleteByUid(uid);
        wallet.setBalance(zero());
        return toBalance(walletRepository.save(wallet));
    }

    @Transactional
    public WalletResult bet(String providerCode, String sessionId, String transactionId, BigDecimal amount) {
        BigDecimal stake = positive(amount);
        WalletResult replayed = replayBet(transactionId, sessionId, stake);
        if (replayed != null) {
            return replayed;
        }

        GameSession session = sessionService.requireActive(sessionId, providerCode);
        DemoWallet wallet = lockWallet(session.getUid());
        replayed = replayBet(transactionId, sessionId, stake);
        if (replayed != null) {
            return replayed;
        }

        if (wallet.getBalance().compareTo(stake) < 0) {
            throw new BusinessException("余额不足");
        }

        BigDecimal before = wallet.getBalance();
        BigDecimal after = before.subtract(stake);
        wallet.setBalance(after);
        transactionRepository.save(new WalletTransaction(
                transactionId,
                wallet.getUid(),
                session.getSessionId(),
                TransactionType.BET,
                stake.negate(),
                before,
                after,
                null
        ));
        return WalletResult.success(transactionId, after);
    }

    @Transactional
    public WalletResult settle(
            String providerCode,
            String sessionId,
            String transactionId,
            String relatedTransactionId,
            BigDecimal amount
    ) {
        BigDecimal win = nonNegative(amount);
        WalletResult replayed = replaySettle(transactionId, sessionId, relatedTransactionId, win);
        if (replayed != null) {
            return replayed;
        }

        GameSession session = sessionService.requireActive(sessionId, providerCode);
        DemoWallet wallet = lockWallet(session.getUid());
        replayed = replaySettle(transactionId, sessionId, relatedTransactionId, win);
        if (replayed != null) {
            return replayed;
        }

        WalletTransaction bet = requireBet(relatedTransactionId, session);
        if (hasType(bet.getTransactionId(), TransactionType.ROLLBACK)) {
            throw new BusinessException("该注单已撤单，不能结算");
        }
        if (hasType(bet.getTransactionId(), TransactionType.SETTLE)) {
            throw new BusinessException("该注单已结算，不能重复结算");
        }

        BigDecimal before = wallet.getBalance();
        BigDecimal after = before.add(win);
        wallet.setBalance(after);
        transactionRepository.save(new WalletTransaction(
                transactionId,
                wallet.getUid(),
                session.getSessionId(),
                TransactionType.SETTLE,
                win,
                before,
                after,
                relatedTransactionId
        ));
        return WalletResult.success(transactionId, after);
    }

    @Transactional
    public WalletResult rollback(
            String providerCode,
            String sessionId,
            String transactionId,
            String relatedTransactionId
    ) {
        WalletResult replayed = replayRollback(transactionId, sessionId, relatedTransactionId);
        if (replayed != null) {
            return replayed;
        }

        GameSession session = sessionService.requireForProvider(sessionId, providerCode);
        DemoWallet wallet = lockWallet(session.getUid());
        replayed = replayRollback(transactionId, sessionId, relatedTransactionId);
        if (replayed != null) {
            return replayed;
        }

        WalletTransaction bet = requireBet(relatedTransactionId, session);
        if (hasType(bet.getTransactionId(), TransactionType.SETTLE)) {
            throw new BusinessException("该注单已结算，不能直接撤单");
        }
        if (hasType(bet.getTransactionId(), TransactionType.ROLLBACK)) {
            return WalletResult.success(transactionId, wallet.getBalance());
        }

        BigDecimal refund = bet.getAmount().negate();
        BigDecimal before = wallet.getBalance();
        BigDecimal after = before.add(refund);
        wallet.setBalance(after);
        transactionRepository.save(new WalletTransaction(
                transactionId,
                wallet.getUid(),
                session.getSessionId(),
                TransactionType.ROLLBACK,
                refund,
                before,
                after,
                bet.getTransactionId()
        ));
        return WalletResult.success(transactionId, after);
    }

    private WalletResult replayBet(String transactionId, String sessionId, BigDecimal stake) {
        WalletTransaction existing = findExisting(transactionId);
        if (existing == null) {
            return null;
        }
        boolean same = existing.getType() == TransactionType.BET
                && sessionId.equals(existing.getSessionId())
                && sameMoney(stake.negate(), existing.getAmount())
                && sameUid(sessionId, existing.getUid());
        if (!same) {
            throw new BusinessException(MISMATCH);
        }
        return WalletResult.success(existing.getTransactionId(), existing.getBalanceAfter());
    }

    private WalletResult replaySettle(
            String transactionId,
            String sessionId,
            String relatedTransactionId,
            BigDecimal amount
    ) {
        WalletTransaction existing = findExisting(transactionId);
        if (existing == null) {
            return null;
        }
        boolean same = existing.getType() == TransactionType.SETTLE
                && sessionId.equals(existing.getSessionId())
                && relatedTransactionId.equals(existing.getRelatedTransactionId())
                && sameMoney(amount, existing.getAmount())
                && sameUid(sessionId, existing.getUid());
        if (!same) {
            throw new BusinessException(MISMATCH);
        }
        return WalletResult.success(existing.getTransactionId(), existing.getBalanceAfter());
    }

    private WalletResult replayRollback(String transactionId, String sessionId, String relatedTransactionId) {
        WalletTransaction existing = findExisting(transactionId);
        if (existing == null) {
            return null;
        }
        boolean same = existing.getType() == TransactionType.ROLLBACK
                && sessionId.equals(existing.getSessionId())
                && relatedTransactionId.equals(existing.getRelatedTransactionId())
                && sameUid(sessionId, existing.getUid());
        if (!same) {
            throw new BusinessException(MISMATCH);
        }
        return WalletResult.success(existing.getTransactionId(), existing.getBalanceAfter());
    }

    private WalletTransaction findExisting(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId).orElse(null);
    }

    private boolean hasType(String relatedTransactionId, TransactionType type) {
        return transactionRepository.existsByRelatedTransactionIdAndType(relatedTransactionId, type);
    }

    private boolean sameUid(String sessionId, String uid) {
        return sessionService.find(sessionId)
                .map(session -> uid.equals(session.getUid()))
                .orElse(true);
    }

    private boolean sameMoney(BigDecimal left, BigDecimal right) {
        return left.compareTo(right) == 0;
    }

    private WalletTransaction requireBet(String relatedTransactionId, GameSession session) {
        WalletTransaction bet = transactionRepository.findByTransactionId(relatedTransactionId)
                .orElseThrow(() -> new BusinessException("关联注单不存在"));
        if (bet.getType() != TransactionType.BET) {
            throw new BusinessException("关联交易不是注单");
        }
        if (!session.getSessionId().equals(bet.getSessionId()) || !session.getUid().equals(bet.getUid())) {
            throw new BusinessException("关联注单不属于当前会话");
        }
        return bet;
    }

    private DemoWallet lockWallet(String uid) {
        return walletRepository.findByUidForUpdate(uid)
                .orElseThrow(() -> new BusinessException("钱包不存在"));
    }

    private DemoWallet requireWallet(String uid) {
        return walletRepository.findByUid(uid)
                .orElseThrow(() -> new BusinessException("钱包不存在"));
    }

    private BalanceResponse keepServerBalance(DemoWallet wallet, String username) {
        if (username != null && !username.isBlank()) {
            wallet.setUsername(username.trim());
        }
        return toBalance(wallet);
    }

    private BalanceResponse toBalance(DemoWallet wallet) {
        return new BalanceResponse(wallet.getUid(), wallet.getBalance());
    }

    private BigDecimal nonNegative(BigDecimal value) {
        BigDecimal normalized = scale(value);
        if (normalized.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("金额不能为负数");
        }
        return normalized;
    }

    private BigDecimal positive(BigDecimal value) {
        BigDecimal normalized = scale(value);
        if (normalized.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("金额必须大于0");
        }
        return normalized;
    }

    private BigDecimal scale(BigDecimal value) {
        if (value == null) {
            throw new BusinessException("金额不能为空");
        }
        return value.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal zero() {
        return BigDecimal.ZERO.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }
}
