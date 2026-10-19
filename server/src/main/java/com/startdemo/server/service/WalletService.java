package com.startdemo.server.service;

import com.startdemo.server.domain.DemoWallet;
import com.startdemo.server.domain.GameSession;
import com.startdemo.server.domain.SessionStatus;
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
import java.util.Optional;

@Service
public class WalletService {

    private static final int MONEY_SCALE = 2;

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
     * 把前端 Mock 余额写入游戏钱包，只为当前 Demo 衔接。
     * 正式生产系统中，余额必须由服务器作为唯一数据源，不会由前端上传余额覆盖服务器。
     */
    @Transactional
    public BalanceResponse syncDemoBalance(String uid, String username, BigDecimal balance) {
        BigDecimal normalized = nonNegative(balance);
        DemoWallet wallet = walletRepository.findByUid(uid)
                .orElseGet(() -> new DemoWallet(uid, username, normalized));
        wallet.setUsername(username);
        wallet.setBalance(normalized);
        return toBalance(walletRepository.save(wallet));
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
        Optional<WalletResult> replayed = replay(transactionId, TransactionType.BET);
        if (replayed.isPresent()) {
            return replayed.get();
        }

        GameSession session = sessionService.requireActive(sessionId, providerCode);
        DemoWallet wallet = lockWallet(session.getUid());
        replayed = replay(transactionId, TransactionType.BET);
        if (replayed.isPresent()) {
            return replayed.get();
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
        Optional<WalletResult> replayed = replay(transactionId, TransactionType.SETTLE);
        if (replayed.isPresent()) {
            return replayed.get();
        }

        GameSession session = sessionService.requireActive(sessionId, providerCode);
        DemoWallet wallet = lockWallet(session.getUid());
        replayed = replay(transactionId, TransactionType.SETTLE);
        if (replayed.isPresent()) {
            return replayed.get();
        }

        requireBet(relatedTransactionId, session);
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
        Optional<WalletResult> replayed = replay(transactionId, TransactionType.ROLLBACK);
        if (replayed.isPresent()) {
            return replayed.get();
        }

        GameSession session = sessionService.requireForProvider(sessionId, providerCode);
        DemoWallet wallet = lockWallet(session.getUid());
        replayed = replay(transactionId, TransactionType.ROLLBACK);
        if (replayed.isPresent()) {
            return replayed.get();
        }

        WalletTransaction bet = requireBet(relatedTransactionId, session);
        if (transactionRepository.existsByRelatedTransactionIdAndType(bet.getTransactionId(), TransactionType.ROLLBACK)) {
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

    private Optional<WalletResult> replay(String transactionId, TransactionType expected) {
        return transactionRepository.findByTransactionId(transactionId).map(existing -> {
            if (existing.getType() != expected) {
                throw new BusinessException("交易号已存在");
            }
            return WalletResult.success(existing.getTransactionId(), existing.getBalanceAfter());
        });
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
