package com.startdemo.server.service;

import com.startdemo.server.error.BusinessException;
import com.startdemo.server.repository.DemoWalletRepository;
import com.startdemo.server.repository.DepositOrderRepository;
import com.startdemo.server.repository.WithdrawalOrderRepository;
import com.startdemo.server.web.payment.dto.DepositOrderResponse;
import com.startdemo.server.web.payment.dto.WithdrawalOrderResponse;
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
class PaymentServiceTest {

    private static final String UID = "161936";

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private AdminQueryService adminQueryService;

    @Autowired
    private DemoWalletRepository walletRepository;

    @Autowired
    private DepositOrderRepository depositRepository;

    @Autowired
    private WithdrawalOrderRepository withdrawalRepository;

    @BeforeEach
    void clean() {
        depositRepository.deleteAll();
        withdrawalRepository.deleteAll();
        walletRepository.deleteAll();
        walletService.syncDemoBalance(UID, "DemoPlayer", new BigDecimal("1888.66"));
    }

    @Test
    void createDepositKeepsBalancePending() {
        DepositOrderResponse order = paymentService.createDeposit(UID, "USDT", new BigDecimal("100"));

        assertThat(order.status()).isEqualTo("PENDING");
        assertThat(balance()).isEqualByComparingTo("1888.66");
    }

    @Test
    void approveDepositCreditsOnce() {
        DepositOrderResponse order = paymentService.createDeposit(UID, "USDT", new BigDecimal("100"));

        DepositOrderResponse approved = paymentService.approveDeposit(order.orderNo(), null);

        assertThat(approved.status()).isEqualTo("APPROVED");
        assertThat(balance()).isEqualByComparingTo("1988.66");
        assertThatThrownBy(() -> paymentService.approveDeposit(order.orderNo(), null))
                .isInstanceOf(BusinessException.class)
                .hasMessage("订单已处理");
        assertThat(balance()).isEqualByComparingTo("1988.66");
    }

    @Test
    void rejectDepositDoesNotChangeBalance() {
        DepositOrderResponse order = paymentService.createDeposit(UID, "X_WALLET", new BigDecimal("100"));

        DepositOrderResponse rejected = paymentService.rejectDeposit(order.orderNo(), "Demo reject");

        assertThat(rejected.status()).isEqualTo("REJECTED");
        assertThat(rejected.remark()).isEqualTo("Demo reject");
        assertThat(balance()).isEqualByComparingTo("1888.66");
        assertThatThrownBy(() -> paymentService.approveDeposit(order.orderNo(), null))
                .isInstanceOf(BusinessException.class)
                .hasMessage("订单已处理");
    }

    @Test
    void createWithdrawalDebitsImmediately() {
        WithdrawalOrderResponse order = paymentService.createWithdrawal(UID, "USDT", new BigDecimal("100"), "Tdemo");

        assertThat(order.status()).isEqualTo("PENDING");
        assertThat(order.fee()).isEqualByComparingTo("0.00");
        assertThat(balance()).isEqualByComparingTo("1788.66");
    }

    @Test
    void withdrawalFailsWhenBalanceIsInsufficient() {
        assertThatThrownBy(() -> paymentService.createWithdrawal(UID, "USDT", new BigDecimal("2000"), "Tdemo"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("余额不足");

        assertThat(balance()).isEqualByComparingTo("1888.66");
        assertThat(withdrawalRepository.count()).isZero();
    }

    @Test
    void approveWithdrawalDoesNotDebitAgain() {
        WithdrawalOrderResponse order = paymentService.createWithdrawal(UID, "X_WALLET", new BigDecimal("100"), null);

        WithdrawalOrderResponse approved = paymentService.approveWithdrawal(order.orderNo(), null);

        assertThat(approved.status()).isEqualTo("APPROVED");
        assertThat(balance()).isEqualByComparingTo("1788.66");
        assertThatThrownBy(() -> paymentService.rejectWithdrawal(order.orderNo(), "late"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("订单已处理");
        assertThat(balance()).isEqualByComparingTo("1788.66");
    }

    @Test
    void rejectWithdrawalRefundsOnce() {
        WithdrawalOrderResponse order = paymentService.createWithdrawal(UID, "USDT", new BigDecimal("100"), "Tdemo");

        WithdrawalOrderResponse rejected = paymentService.rejectWithdrawal(order.orderNo(), "Demo reject");

        assertThat(rejected.status()).isEqualTo("REJECTED");
        assertThat(balance()).isEqualByComparingTo("1888.66");
        assertThatThrownBy(() -> paymentService.rejectWithdrawal(order.orderNo(), "again"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("订单已处理");
        assertThatThrownBy(() -> paymentService.approveWithdrawal(order.orderNo(), null))
                .isInstanceOf(BusinessException.class)
                .hasMessage("订单已处理");
        assertThat(balance()).isEqualByComparingTo("1888.66");
    }

    @Test
    void dashboardCountsPendingOrdersAndFiltersByStatus() {
        paymentService.createDeposit(UID, "USDT", new BigDecimal("100"));
        DepositOrderResponse second = paymentService.createDeposit(UID, "USDT", new BigDecimal("50"));
        paymentService.approveDeposit(second.orderNo(), null);
        paymentService.createWithdrawal(UID, "USDT", new BigDecimal("20"), "Tdemo");
        WithdrawalOrderResponse done = paymentService.createWithdrawal(UID, "USDT", new BigDecimal("30"), "Tdemo");
        paymentService.approveWithdrawal(done.orderNo(), null);

        assertThat(adminQueryService.dashboard().pendingDeposits()).isEqualTo(1);
        assertThat(adminQueryService.dashboard().pendingWithdrawals()).isEqualTo(1);
        assertThat(adminQueryService.deposits(UID, "PENDING", 50))
                .extracting(DepositOrderResponse::status)
                .containsExactly("PENDING");
        assertThat(adminQueryService.withdrawals(null, "APPROVED", 50))
                .extracting(WithdrawalOrderResponse::status)
                .containsExactly("APPROVED");
    }

    private BigDecimal balance() {
        return walletService.getBalance(UID).balance();
    }
}
