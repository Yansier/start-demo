package com.startdemo.server.service;

import com.startdemo.server.domain.PaymentStatus;
import com.startdemo.server.error.BusinessException;
import com.startdemo.server.repository.DemoWalletRepository;
import com.startdemo.server.repository.DepositOrderRepository;
import com.startdemo.server.repository.WithdrawalOrderRepository;
import com.startdemo.server.web.payment.dto.DepositOrderResponse;
import com.startdemo.server.web.payment.dto.WithdrawalOrderResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class PaymentConcurrencyTest {

    private static final String UID = "161936";

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private DemoWalletRepository walletRepository;

    @Autowired
    private DepositOrderRepository depositRepository;

    @Autowired
    private WithdrawalOrderRepository withdrawalRepository;

    private ExecutorService pool;

    @BeforeEach
    void clean() {
        depositRepository.deleteAll();
        withdrawalRepository.deleteAll();
        walletRepository.deleteAll();
        walletService.syncDemoBalance(UID, "DemoPlayer", new BigDecimal("1888.66"));
        pool = Executors.newFixedThreadPool(2);
    }

    @AfterEach
    void shutdown() {
        pool.shutdownNow();
    }

    @Test
    void concurrentApproveDepositCreditsOnce() throws Exception {
        DepositOrderResponse order = paymentService.createDeposit(UID, "USDT", new BigDecimal("100"));

        Outcome outcome = race(
                () -> paymentService.approveDeposit(order.orderNo(), null),
                () -> paymentService.approveDeposit(order.orderNo(), null)
        );

        assertThat(outcome.success()).isEqualTo(1);
        assertThat(outcome.failure()).isEqualTo(1);
        assertThat(outcome.failureMessage()).isEqualTo("订单已处理");
        assertThat(depositStatus(order.orderNo())).isEqualTo(PaymentStatus.APPROVED);
        assertThat(balance()).isEqualByComparingTo("1988.66");
    }

    @Test
    void concurrentRejectWithdrawalRefundsOnce() throws Exception {
        WithdrawalOrderResponse order = paymentService.createWithdrawal(UID, "USDT", new BigDecimal("100"), "Tdemo");
        assertThat(balance()).isEqualByComparingTo("1788.66");

        Outcome outcome = race(
                () -> paymentService.rejectWithdrawal(order.orderNo(), "Demo reject"),
                () -> paymentService.rejectWithdrawal(order.orderNo(), "Demo reject")
        );

        assertThat(outcome.success()).isEqualTo(1);
        assertThat(outcome.failure()).isEqualTo(1);
        assertThat(outcome.failureMessage()).isEqualTo("订单已处理");
        assertThat(withdrawalStatus(order.orderNo())).isEqualTo(PaymentStatus.REJECTED);
        assertThat(balance()).isEqualByComparingTo("1888.66");
    }

    @Test
    void concurrentApproveAndRejectDepositHasOneTerminalState() throws Exception {
        DepositOrderResponse order = paymentService.createDeposit(UID, "USDT", new BigDecimal("100"));

        Outcome outcome = race(
                () -> paymentService.approveDeposit(order.orderNo(), null),
                () -> paymentService.rejectDeposit(order.orderNo(), "Demo reject")
        );

        assertThat(outcome.success()).isEqualTo(1);
        assertThat(outcome.failure()).isEqualTo(1);
        assertThat(outcome.failureMessage()).isEqualTo("订单已处理");
        PaymentStatus status = depositStatus(order.orderNo());
        assertThat(status).isIn(PaymentStatus.APPROVED, PaymentStatus.REJECTED);
        if (status == PaymentStatus.APPROVED) {
            assertThat(balance()).isEqualByComparingTo("1988.66");
        } else {
            assertThat(balance()).isEqualByComparingTo("1888.66");
        }
    }

    @Test
    void concurrentApproveAndRejectWithdrawalHasOneTerminalState() throws Exception {
        WithdrawalOrderResponse order = paymentService.createWithdrawal(UID, "USDT", new BigDecimal("100"), "Tdemo");

        Outcome outcome = race(
                () -> paymentService.approveWithdrawal(order.orderNo(), null),
                () -> paymentService.rejectWithdrawal(order.orderNo(), "Demo reject")
        );

        assertThat(outcome.success()).isEqualTo(1);
        assertThat(outcome.failure()).isEqualTo(1);
        assertThat(outcome.failureMessage()).isEqualTo("订单已处理");
        PaymentStatus status = withdrawalStatus(order.orderNo());
        assertThat(status).isIn(PaymentStatus.APPROVED, PaymentStatus.REJECTED);
        if (status == PaymentStatus.APPROVED) {
            assertThat(balance()).isEqualByComparingTo("1788.66");
        } else {
            assertThat(balance()).isEqualByComparingTo("1888.66");
        }
    }

    private Outcome race(Runnable left, Runnable right) throws Exception {
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch startGate = new CountDownLatch(1);
        Future<?> first = pool.submit(gated(left, ready, startGate));
        Future<?> second = pool.submit(gated(right, ready, startGate));
        assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();
        startGate.countDown();

        int success = 0;
        int failure = 0;
        String failureMessage = null;
        for (Future<?> future : new Future<?>[] {first, second}) {
            try {
                future.get(10, TimeUnit.SECONDS);
                success++;
            } catch (ExecutionException ex) {
                Throwable cause = ex.getCause();
                assertThat(cause).isInstanceOf(BusinessException.class);
                failure++;
                failureMessage = cause.getMessage();
            }
        }
        return new Outcome(success, failure, failureMessage);
    }

    private Runnable gated(Runnable action, CountDownLatch ready, CountDownLatch startGate) {
        return () -> {
            ready.countDown();
            try {
                if (!startGate.await(5, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("审核线程未同时启动");
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(ex);
            }
            action.run();
        };
    }

    private BigDecimal balance() {
        return walletService.getBalance(UID).balance();
    }

    private PaymentStatus depositStatus(String orderNo) {
        return depositRepository.findByOrderNo(orderNo).orElseThrow().getStatus();
    }

    private PaymentStatus withdrawalStatus(String orderNo) {
        return withdrawalRepository.findByOrderNo(orderNo).orElseThrow().getStatus();
    }

    private record Outcome(int success, int failure, String failureMessage) {
    }
}
