package com.startdemo.server.web;

import com.startdemo.server.service.WalletService;
import com.startdemo.server.web.dto.BalanceResponse;
import com.startdemo.server.web.dto.TransactionResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{uid}/balance")
    public BalanceResponse balance(@PathVariable String uid) {
        return walletService.getBalance(uid);
    }

    @GetMapping("/{uid}/transactions")
    public List<TransactionResponse> transactions(@PathVariable String uid) {
        return walletService.listTransactions(uid);
    }
}
