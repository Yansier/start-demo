package com.startdemo.server.service;

import com.startdemo.server.domain.DemoWallet;
import com.startdemo.server.domain.SessionStatus;
import com.startdemo.server.domain.TransactionType;
import com.startdemo.server.error.BusinessException;
import com.startdemo.server.provider.MockGameProvider;
import com.startdemo.server.repository.DemoWalletRepository;
import com.startdemo.server.repository.GameSessionRepository;
import com.startdemo.server.repository.WalletTransactionRepository;
import com.startdemo.server.web.admin.dto.AdminDashboardResponse;
import com.startdemo.server.web.admin.dto.AdminSessionResponse;
import com.startdemo.server.web.admin.dto.AdminSystemStatusResponse;
import com.startdemo.server.web.admin.dto.AdminTransactionResponse;
import com.startdemo.server.web.admin.dto.AdminUserDetailResponse;
import com.startdemo.server.web.admin.dto.AdminUserResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DEMO ONLY，只读查询。
 * 公网开放管理接口之前必须增加访问保护。本服务不修改余额、流水或会话。
 */
@Service
@Transactional(readOnly = true)
public class AdminQueryService {

    private static final int DEFAULT_LIMIT = 100;
    private static final int MAX_LIMIT = 200;
    private static final int RECENT_LIMIT = 20;

    private final DemoWalletRepository walletRepository;
    private final GameSessionRepository sessionRepository;
    private final WalletTransactionRepository transactionRepository;

    public AdminQueryService(
            DemoWalletRepository walletRepository,
            GameSessionRepository sessionRepository,
            WalletTransactionRepository transactionRepository
    ) {
        this.walletRepository = walletRepository;
        this.sessionRepository = sessionRepository;
        this.transactionRepository = transactionRepository;
    }

    public AdminDashboardResponse dashboard() {
        LocalDateTime from = LocalDate.now().atStartOfDay();
        LocalDateTime to = from.plusDays(1);
        BigDecimal betAmount = money(transactionRepository.sumAmountByTypeCreatedBetween(TransactionType.BET, from, to));
        return new AdminDashboardResponse(
                walletRepository.count(),
                money(walletRepository.sumBalance()),
                sessionRepository.countByStatus(SessionStatus.ACTIVE),
                transactionRepository.countByTypeCreatedBetween(TransactionType.BET, from, to),
                betAmount.abs(),
                transactionRepository.countByTypeCreatedBetween(TransactionType.SETTLE, from, to),
                money(transactionRepository.sumAmountByTypeCreatedBetween(TransactionType.SETTLE, from, to)),
                transactionRepository.countByTypeCreatedBetween(TransactionType.ROLLBACK, from, to),
                transactionRepository.countCreatedBetween(from, to)
        );
    }

    public List<AdminUserResponse> users() {
        return walletRepository.findAllByOrderByUpdatedAtDesc().stream()
                .map(AdminUserResponse::from)
                .toList();
    }

    public AdminUserDetailResponse userDetail(String uid) {
        DemoWallet wallet = walletRepository.findByUid(uid)
                .orElseThrow(() -> new BusinessException("钱包不存在"));
        List<AdminTransactionResponse> transactions = transactionRepository.findTop20ByUidOrderByCreatedAtDesc(uid).stream()
                .map(AdminTransactionResponse::from)
                .toList();
        List<AdminSessionResponse> sessions = sessionRepository.findTop20ByUidOrderByUpdatedAtDesc(uid).stream()
                .map(AdminSessionResponse::from)
                .toList();
        return new AdminUserDetailResponse(AdminUserResponse.from(wallet), transactions, sessions);
    }

    public List<AdminTransactionResponse> transactions(String uid, String type, Integer limit) {
        return transactionRepository.search(blankToNull(uid), parseType(type), PageRequest.of(0, normalizeLimit(limit)))
                .stream()
                .map(AdminTransactionResponse::from)
                .toList();
    }

    public AdminSystemStatusResponse status() {
        return new AdminSystemStatusResponse(
                "UP",
                "start-demo-server",
                "H2",
                MockGameProvider.CODE,
                LocalDateTime.now(),
                "0.1.0"
        );
    }

    private TransactionType parseType(String type) {
        String value = blankToNull(type);
        if (value == null) {
            return null;
        }
        try {
            return TransactionType.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new BusinessException("流水类型不正确");
        }
    }

    private int normalizeLimit(Integer limit) {
        if (limit == null) {
            return DEFAULT_LIMIT;
        }
        return Math.min(Math.max(limit, 1), MAX_LIMIT);
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private BigDecimal money(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
