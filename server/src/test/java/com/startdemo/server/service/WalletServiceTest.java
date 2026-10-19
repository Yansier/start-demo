package com.startdemo.server.service;

import com.startdemo.server.domain.TransactionType;
import com.startdemo.server.error.BusinessException;
import com.startdemo.server.provider.MockGameProvider;
import com.startdemo.server.repository.WalletTransactionRepository;
import com.startdemo.server.web.dto.WalletResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class WalletServiceTest {

    private static final String UID = "161936";
    private static final BigDecimal OPENING = new BigDecimal("1888.66");

    @Autowired
    private WalletService walletService;

    @Autowired
    private GameSessionService sessionService;

    @Autowired
    private WalletTransactionRepository transactionRepository;

    @BeforeEach
    void clean() {
        transactionRepository.deleteAll();
        sessionService.deleteByUid(UID);
        walletService.syncDemoBalance(UID, "DemoPlayer", BigDecimal.ZERO);
    }

    @Test
    void betDeductsBalance() {
        String sessionId = openSession(OPENING);

        WalletResult result = walletService.bet(MockGameProvider.CODE, sessionId, "BET-001", new BigDecimal("50"));

        assertThat(result.status()).isEqualTo("SUCCESS");
        assertThat(result.balance()).isEqualByComparingTo("1838.66");
        assertThat(walletService.getBalance(UID).balance()).isEqualByComparingTo("1838.66");
        assertThat(transactionRepository.findByTransactionId("BET-001").orElseThrow().getAmount())
                .isEqualByComparingTo("-50.00");
    }

    @Test
    void duplicateBetDoesNotDebitAgain() {
        String sessionId = openSession(OPENING);

        walletService.bet(MockGameProvider.CODE, sessionId, "BET-001", new BigDecimal("50"));
        WalletResult replay = walletService.bet(MockGameProvider.CODE, sessionId, "BET-001", new BigDecimal("50"));

        assertThat(replay.balance()).isEqualByComparingTo("1838.66");
        assertThat(walletService.getBalance(UID).balance()).isEqualByComparingTo("1838.66");
        assertThat(transactionRepository.findByUidOrderByCreatedAtDesc(UID)).hasSize(1);
    }

    @Test
    void settleIncreasesBalance() {
        String sessionId = openSession(OPENING);
        walletService.bet(MockGameProvider.CODE, sessionId, "BET-001", new BigDecimal("50"));

        WalletResult result = walletService.settle(
                MockGameProvider.CODE, sessionId, "SETTLE-001", "BET-001", new BigDecimal("100"));

        assertThat(result.balance()).isEqualByComparingTo("1938.66");
        assertThat(walletService.getBalance(UID).balance()).isEqualByComparingTo("1938.66");
        assertThat(transactionRepository.findByTransactionId("SETTLE-001").orElseThrow().getType())
                .isEqualTo(TransactionType.SETTLE);
    }

    @Test
    void rollbackRefundsBet() {
        String sessionId = openSession(OPENING);
        walletService.bet(MockGameProvider.CODE, sessionId, "BET-001", new BigDecimal("50"));

        WalletResult result = walletService.rollback(MockGameProvider.CODE, sessionId, "ROLLBACK-001", "BET-001");

        assertThat(result.balance()).isEqualByComparingTo("1888.66");
        assertThat(transactionRepository.findByTransactionId("ROLLBACK-001").orElseThrow().getAmount())
                .isEqualByComparingTo("50.00");
    }

    @Test
    void duplicateRollbackDoesNotCreditAgain() {
        String sessionId = openSession(OPENING);
        walletService.bet(MockGameProvider.CODE, sessionId, "BET-001", new BigDecimal("50"));
        walletService.rollback(MockGameProvider.CODE, sessionId, "ROLLBACK-001", "BET-001");

        WalletResult sameId = walletService.rollback(MockGameProvider.CODE, sessionId, "ROLLBACK-001", "BET-001");
        WalletResult anotherId = walletService.rollback(MockGameProvider.CODE, sessionId, "ROLLBACK-002", "BET-001");

        assertThat(sameId.balance()).isEqualByComparingTo("1888.66");
        assertThat(anotherId.balance()).isEqualByComparingTo("1888.66");
        assertThat(walletService.getBalance(UID).balance()).isEqualByComparingTo("1888.66");
        assertThat(transactionRepository.findByUidOrderByCreatedAtDesc(UID))
                .filteredOn(tx -> tx.getType() == TransactionType.ROLLBACK)
                .hasSize(1);
    }

    @Test
    void betFailsWhenBalanceIsInsufficient() {
        String sessionId = openSession(OPENING);

        assertThatThrownBy(() -> walletService.bet(MockGameProvider.CODE, sessionId, "BET-LOW", new BigDecimal("2000")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("余额不足");

        assertThat(walletService.getBalance(UID).balance()).isEqualByComparingTo("1888.66");
        assertThat(transactionRepository.findByTransactionId("BET-LOW")).isEmpty();
    }

    private String openSession(BigDecimal balance) {
        walletService.syncDemoBalance(UID, "DemoPlayer", balance);
        return sessionService.launch(UID, "demo-slots", MockGameProvider.CODE).getSessionId();
    }
}
