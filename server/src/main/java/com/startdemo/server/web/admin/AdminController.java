package com.startdemo.server.web.admin;

import com.startdemo.server.service.AdminQueryService;
import com.startdemo.server.service.PaymentService;
import com.startdemo.server.web.admin.dto.AdminDashboardResponse;
import com.startdemo.server.web.admin.dto.AdminSystemStatusResponse;
import com.startdemo.server.web.admin.dto.AdminTransactionResponse;
import com.startdemo.server.web.admin.dto.AdminUserDetailResponse;
import com.startdemo.server.web.admin.dto.AdminUserResponse;
import com.startdemo.server.web.admin.dto.ReviewRequest;
import com.startdemo.server.web.payment.dto.DepositOrderResponse;
import com.startdemo.server.web.payment.dto.WithdrawalOrderResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * DEMO ONLY。
 * 查询接口和充值提现审核都没有登录保护。审核接口是写操作，增加访问保护之前不能公开部署。
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminQueryService adminQueryService;
    private final PaymentService paymentService;

    public AdminController(AdminQueryService adminQueryService, PaymentService paymentService) {
        this.adminQueryService = adminQueryService;
        this.paymentService = paymentService;
    }

    @GetMapping("/dashboard")
    public AdminDashboardResponse dashboard() {
        return adminQueryService.dashboard();
    }

    @GetMapping("/users")
    public List<AdminUserResponse> users() {
        return adminQueryService.users();
    }

    @GetMapping("/users/{uid}")
    public AdminUserDetailResponse user(@PathVariable String uid) {
        return adminQueryService.userDetail(uid);
    }

    @GetMapping("/transactions")
    public List<AdminTransactionResponse> transactions(
            @RequestParam(required = false) String uid,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer limit
    ) {
        return adminQueryService.transactions(uid, type, limit);
    }

    @GetMapping("/status")
    public AdminSystemStatusResponse status() {
        return adminQueryService.status();
    }

    @GetMapping("/deposits")
    public List<DepositOrderResponse> deposits(
            @RequestParam(required = false) String uid,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer limit
    ) {
        return adminQueryService.deposits(uid, status, limit);
    }

    @PostMapping("/deposits/{orderNo}/approve")
    public DepositOrderResponse approveDeposit(
            @PathVariable String orderNo,
            @RequestBody(required = false) ReviewRequest request
    ) {
        return paymentService.approveDeposit(orderNo, remark(request));
    }

    @PostMapping("/deposits/{orderNo}/reject")
    public DepositOrderResponse rejectDeposit(
            @PathVariable String orderNo,
            @RequestBody(required = false) ReviewRequest request
    ) {
        return paymentService.rejectDeposit(orderNo, remark(request));
    }

    @GetMapping("/withdrawals")
    public List<WithdrawalOrderResponse> withdrawals(
            @RequestParam(required = false) String uid,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer limit
    ) {
        return adminQueryService.withdrawals(uid, status, limit);
    }

    @PostMapping("/withdrawals/{orderNo}/approve")
    public WithdrawalOrderResponse approveWithdrawal(
            @PathVariable String orderNo,
            @RequestBody(required = false) ReviewRequest request
    ) {
        return paymentService.approveWithdrawal(orderNo, remark(request));
    }

    @PostMapping("/withdrawals/{orderNo}/reject")
    public WithdrawalOrderResponse rejectWithdrawal(
            @PathVariable String orderNo,
            @RequestBody(required = false) ReviewRequest request
    ) {
        return paymentService.rejectWithdrawal(orderNo, remark(request));
    }

    private String remark(ReviewRequest request) {
        return request == null ? null : request.remark();
    }
}
