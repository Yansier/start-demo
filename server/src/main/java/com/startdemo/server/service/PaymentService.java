package com.startdemo.server.service;

import com.startdemo.server.domain.DemoWallet;
import com.startdemo.server.domain.DepositOrder;
import com.startdemo.server.domain.PaymentChannel;
import com.startdemo.server.domain.PaymentStatus;
import com.startdemo.server.domain.WithdrawalOrder;
import com.startdemo.server.error.BusinessException;
import com.startdemo.server.repository.DemoWalletRepository;
import com.startdemo.server.repository.DepositOrderRepository;
import com.startdemo.server.repository.WithdrawalOrderRepository;
import com.startdemo.server.web.payment.dto.DepositOrderResponse;
import com.startdemo.server.web.payment.dto.WithdrawalOrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * DEMO ONLY。
 * 充值提现是演示订单，没有真实支付，也没有用户认证。uid 来自请求。
 * 审核写接口在增加访问保护之前不能公开部署。
 */
@Service
public class PaymentService {

    private static final BigDecimal MIN_AMOUNT = new BigDecimal("10.00");
    private static final DateTimeFormatter DAY = DateTimeFormatter.BASIC_ISO_DATE;

    private final DemoWalletRepository walletRepository;
    private final DepositOrderRepository depositRepository;
    private final WithdrawalOrderRepository withdrawalRepository;

    public PaymentService(
            DemoWalletRepository walletRepository,
            DepositOrderRepository depositRepository,
            WithdrawalOrderRepository withdrawalRepository
    ) {
        this.walletRepository = walletRepository;
        this.depositRepository = depositRepository;
        this.withdrawalRepository = withdrawalRepository;
    }

    @Transactional
    public DepositOrderResponse createDeposit(String uid, String channel, BigDecimal amount) {
        PaymentChannel parsed = parseChannel(channel);
        BigDecimal value = minimum(amount, "最低充值为10");
        requireWallet(uid);
        DepositOrder order = depositRepository.save(new DepositOrder(orderNo("DEP"), uid, parsed, value));
        return DepositOrderResponse.from(order);
    }

    public List<DepositOrderResponse> listDeposits(String uid) {
        requireWallet(uid);
        return depositRepository.findByUidOrderByCreatedAtDesc(uid).stream()
                .map(DepositOrderResponse::from)
                .toList();
    }

    @Transactional
    public DepositOrderResponse approveDeposit(String orderNo, String remark) {
        DepositOrder current = requireDeposit(orderNo);
        DemoWallet wallet = lockWallet(current.getUid());
        DepositOrder order = lockDeposit(orderNo);
        requirePending(order);
        wallet.setBalance(wallet.getBalance().add(order.getAmount()));
        finish(order, PaymentStatus.APPROVED, remark);
        return DepositOrderResponse.from(order);
    }

    @Transactional
    public DepositOrderResponse rejectDeposit(String orderNo, String remark) {
        requireDeposit(orderNo);
        DepositOrder order = lockDeposit(orderNo);
        requirePending(order);
        finish(order, PaymentStatus.REJECTED, remark);
        return DepositOrderResponse.from(order);
    }

    @Transactional
    public WithdrawalOrderResponse createWithdrawal(String uid, String channel, BigDecimal amount, String address) {
        PaymentChannel parsed = parseChannel(channel);
        BigDecimal value = minimum(amount, "最低提现为10");
        String payoutAddress = addressFor(parsed, address);
        DemoWallet wallet = lockWallet(uid);
        if (wallet.getBalance().compareTo(value) < 0) {
            throw new BusinessException("余额不足");
        }
        wallet.setBalance(wallet.getBalance().subtract(value));
        WithdrawalOrder order = withdrawalRepository.save(new WithdrawalOrder(
                orderNo("WD"),
                uid,
                parsed,
                value,
                zero(),
                payoutAddress
        ));
        return WithdrawalOrderResponse.from(order);
    }

    public List<WithdrawalOrderResponse> listWithdrawals(String uid) {
        requireWallet(uid);
        return withdrawalRepository.findByUidOrderByCreatedAtDesc(uid).stream()
                .map(WithdrawalOrderResponse::from)
                .toList();
    }

    @Transactional
    public WithdrawalOrderResponse approveWithdrawal(String orderNo, String remark) {
        requireWithdrawal(orderNo);
        WithdrawalOrder order = lockWithdrawal(orderNo);
        requirePending(order);
        finish(order, PaymentStatus.APPROVED, remark);
        return WithdrawalOrderResponse.from(order);
    }

    @Transactional
    public WithdrawalOrderResponse rejectWithdrawal(String orderNo, String remark) {
        WithdrawalOrder current = requireWithdrawal(orderNo);
        DemoWallet wallet = lockWallet(current.getUid());
        WithdrawalOrder order = lockWithdrawal(orderNo);
        requirePending(order);
        wallet.setBalance(wallet.getBalance().add(order.getAmount()));
        finish(order, PaymentStatus.REJECTED, remark);
        return WithdrawalOrderResponse.from(order);
    }

    private void finish(DepositOrder order, PaymentStatus status, String remark) {
        order.setStatus(status);
        order.setReviewedAt(LocalDateTime.now());
        applyRemark(remark, order::setRemark);
    }

    private void finish(WithdrawalOrder order, PaymentStatus status, String remark) {
        order.setStatus(status);
        order.setReviewedAt(LocalDateTime.now());
        applyRemark(remark, order::setRemark);
    }

    private void applyRemark(String remark, java.util.function.Consumer<String> setter) {
        if (remark != null && !remark.isBlank()) {
            setter.accept(remark.trim());
        }
    }

    private void requirePending(DepositOrder order) {
        if (order.getStatus() != PaymentStatus.PENDING) {
            throw new BusinessException("订单已处理");
        }
    }

    private void requirePending(WithdrawalOrder order) {
        if (order.getStatus() != PaymentStatus.PENDING) {
            throw new BusinessException("订单已处理");
        }
    }

    private DepositOrder requireDeposit(String orderNo) {
        return depositRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));
    }

    private DepositOrder lockDeposit(String orderNo) {
        return depositRepository.findByOrderNoForUpdate(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));
    }

    private WithdrawalOrder requireWithdrawal(String orderNo) {
        return withdrawalRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));
    }

    private WithdrawalOrder lockWithdrawal(String orderNo) {
        return withdrawalRepository.findByOrderNoForUpdate(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));
    }

    private DemoWallet requireWallet(String uid) {
        return walletRepository.findByUid(uid)
                .orElseThrow(() -> new BusinessException("钱包不存在"));
    }

    private DemoWallet lockWallet(String uid) {
        return walletRepository.findByUidForUpdate(uid)
                .orElseThrow(() -> new BusinessException("钱包不存在"));
    }

    private PaymentChannel parseChannel(String channel) {
        if (channel == null || channel.isBlank()) {
            throw new BusinessException("渠道不正确");
        }
        try {
            return PaymentChannel.valueOf(channel.trim());
        } catch (IllegalArgumentException ex) {
            throw new BusinessException("渠道不正确");
        }
    }

    private String addressFor(PaymentChannel channel, String address) {
        String value = address == null ? "" : address.trim();
        if (channel == PaymentChannel.USDT && value.isBlank()) {
            throw new BusinessException("USDT地址不能为空");
        }
        return value.isBlank() ? null : value;
    }

    private BigDecimal minimum(BigDecimal amount, String minMessage) {
        if (amount == null) {
            throw new BusinessException("金额不能为空");
        }
        BigDecimal value = amount.setScale(2, RoundingMode.HALF_UP);
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("金额必须大于0");
        }
        if (value.compareTo(MIN_AMOUNT) < 0) {
            throw new BusinessException(minMessage);
        }
        return value;
    }

    private BigDecimal zero() {
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    private String orderNo(String prefix) {
        String tail = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return prefix + "-" + LocalDate.now().format(DAY) + "-" + tail;
    }
}
