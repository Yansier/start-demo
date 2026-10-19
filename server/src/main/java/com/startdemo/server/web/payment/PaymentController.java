package com.startdemo.server.web.payment;

import com.startdemo.server.service.PaymentService;
import com.startdemo.server.web.payment.dto.CreateDepositRequest;
import com.startdemo.server.web.payment.dto.CreateWithdrawalRequest;
import com.startdemo.server.web.payment.dto.DepositOrderResponse;
import com.startdemo.server.web.payment.dto.WithdrawalOrderResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * DEMO ONLY。
 * 当前没有用户认证，uid 由调用方传入。不能当作生产支付接口。
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/deposits")
    public DepositOrderResponse createDeposit(@Valid @RequestBody CreateDepositRequest request) {
        return paymentService.createDeposit(request.uid(), request.channel(), request.amount());
    }

    @GetMapping("/deposits/{uid}")
    public List<DepositOrderResponse> deposits(@PathVariable String uid) {
        return paymentService.listDeposits(uid);
    }

    @PostMapping("/withdrawals")
    public WithdrawalOrderResponse createWithdrawal(@Valid @RequestBody CreateWithdrawalRequest request) {
        return paymentService.createWithdrawal(request.uid(), request.channel(), request.amount(), request.address());
    }

    @GetMapping("/withdrawals/{uid}")
    public List<WithdrawalOrderResponse> withdrawals(@PathVariable String uid) {
        return paymentService.listWithdrawals(uid);
    }
}
