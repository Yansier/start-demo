package com.startdemo.server.web;

import com.startdemo.server.service.WalletService;
import com.startdemo.server.web.dto.BalanceResponse;
import com.startdemo.server.web.dto.SyncWalletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private final WalletService walletService;

    public DemoController(WalletService walletService) {
        this.walletService = walletService;
    }

    /**
     * DEMO ONLY.
     * 进入游戏前，把当前前端 Mock 余额同步到 Java Game Wallet。
     * 正式生产系统中，余额必须由服务器作为唯一数据源，不会由前端上传余额覆盖服务器。
     */
    @PostMapping("/wallet/sync")
    public BalanceResponse sync(@Valid @RequestBody SyncWalletRequest request) {
        return walletService.syncDemoBalance(request.uid(), request.username(), request.balance());
    }

    /**
     * DEMO ONLY.
     * 开发调试用：删除该用户的游戏会话和钱包流水，余额重置为 0。
     * 正式生产系统不应提供由调用方重置余额的接口。
     */
    @PostMapping("/reset/{uid}")
    public BalanceResponse reset(@PathVariable String uid) {
        return walletService.resetDemo(uid);
    }
}
