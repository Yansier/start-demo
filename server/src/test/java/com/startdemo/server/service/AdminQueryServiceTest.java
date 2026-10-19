package com.startdemo.server.service;

import com.startdemo.server.domain.SessionStatus;
import com.startdemo.server.provider.MockGameProvider;
import com.startdemo.server.repository.DemoWalletRepository;
import com.startdemo.server.repository.DepositOrderRepository;
import com.startdemo.server.repository.GameSessionRepository;
import com.startdemo.server.repository.WalletTransactionRepository;
import com.startdemo.server.repository.WithdrawalOrderRepository;
import com.startdemo.server.web.admin.dto.AdminDashboardResponse;
import com.startdemo.server.web.admin.dto.AdminTransactionResponse;
import com.startdemo.server.web.admin.dto.AdminUserDetailResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AdminQueryServiceTest {

    @Autowired
    private AdminQueryService adminQueryService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private GameSessionService sessionService;

    @Autowired
    private DemoWalletRepository walletRepository;

    @Autowired
    private GameSessionRepository sessionRepository;

    @Autowired
    private WalletTransactionRepository transactionRepository;

    @Autowired
    private DepositOrderRepository depositRepository;

    @Autowired
    private WithdrawalOrderRepository withdrawalRepository;

    @BeforeEach
    void clean() {
        depositRepository.deleteAll();
        withdrawalRepository.deleteAll();
        transactionRepository.deleteAll();
        sessionRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    void dashboardCountsWalletsAndPositiveBetAmount() {
        String sessionA = open("161936", "DemoPlayer", "1888.66");
        String sessionB = open("320000", "Other", "1000.00");
        walletService.bet(MockGameProvider.CODE, sessionA, "BET-A1", new BigDecimal("50"));
        walletService.bet(MockGameProvider.CODE, sessionA, "BET-A2", new BigDecimal("100"));
        walletService.settle(MockGameProvider.CODE, sessionA, "SETTLE-A1", "BET-A1", new BigDecimal("80"));
        walletService.bet(MockGameProvider.CODE, sessionB, "BET-B1", new BigDecimal("10"));
        sessionService.close(sessionB);

        AdminDashboardResponse dashboard = adminQueryService.dashboard();

        assertThat(dashboard.walletUsers()).isEqualTo(2);
        assertThat(dashboard.totalBalance()).isEqualByComparingTo("2808.66");
        assertThat(dashboard.activeSessions()).isEqualTo(1);
        assertThat(dashboard.todayBetCount()).isEqualTo(3);
        assertThat(dashboard.todayBetAmount()).isEqualByComparingTo("160.00");
        assertThat(dashboard.todaySettleCount()).isEqualTo(1);
        assertThat(dashboard.todaySettleAmount()).isEqualByComparingTo("80.00");
        assertThat(dashboard.todayRollbackCount()).isZero();
        assertThat(dashboard.todayTransactionCount()).isEqualTo(4);
        assertThat(dashboard.pendingDeposits()).isZero();
        assertThat(dashboard.pendingWithdrawals()).isZero();
        assertThat(sessionRepository.countByStatus(SessionStatus.CLOSED)).isEqualTo(1);
    }

    @Test
    void userDetailOnlyReturnsThatUid() {
        String sessionA = open("161936", "DemoPlayer", "1888.66");
        String sessionB = open("320000", "Other", "1000.00");
        walletService.bet(MockGameProvider.CODE, sessionA, "BET-A", new BigDecimal("50"));
        walletService.bet(MockGameProvider.CODE, sessionB, "BET-B", new BigDecimal("10"));

        AdminUserDetailResponse detail = adminQueryService.userDetail("161936");

        assertThat(detail.wallet().uid()).isEqualTo("161936");
        assertThat(detail.recentTransactions()).extracting(AdminTransactionResponse::uid).containsOnly("161936");
        assertThat(detail.recentTransactions()).extracting(AdminTransactionResponse::transactionId).containsExactly("BET-A");
        assertThat(detail.recentSessions()).hasSize(1);
        assertThat(detail.recentSessions().get(0).sessionId()).isEqualTo(sessionA);
    }

    @Test
    void transactionFilterAndLimit() {
        String sessionId = open("161936", "DemoPlayer", "1888.66");
        walletService.bet(MockGameProvider.CODE, sessionId, "BET-1", new BigDecimal("50"));
        walletService.bet(MockGameProvider.CODE, sessionId, "BET-2", new BigDecimal("20"));
        walletService.settle(MockGameProvider.CODE, sessionId, "SETTLE-1", "BET-1", new BigDecimal("30"));

        assertThat(adminQueryService.transactions(null, "BET", null))
                .extracting(AdminTransactionResponse::type)
                .containsOnly("BET");
        assertThat(adminQueryService.transactions("161936", "SETTLE", 50))
                .extracting(AdminTransactionResponse::transactionId)
                .containsExactly("SETTLE-1");
        assertThat(adminQueryService.transactions(null, null, 1)).hasSize(1);
    }

    private String open(String uid, String username, String balance) {
        walletService.syncDemoBalance(uid, username, new BigDecimal(balance));
        return sessionService.launch(uid, "demo-slots", MockGameProvider.CODE).getSessionId();
    }
}
