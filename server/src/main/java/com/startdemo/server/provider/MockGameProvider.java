package com.startdemo.server.provider;

import com.startdemo.server.service.WalletService;
import com.startdemo.server.web.dto.WalletResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MockGameProvider implements GameProvider {

    public static final String CODE = "MOCK";

    private final WalletService walletService;

    public MockGameProvider(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public String getCode() {
        return CODE;
    }

    @Override
    public WalletResult bet(String sessionId, String transactionId, BigDecimal amount) {
        return walletService.bet(CODE, sessionId, transactionId, amount);
    }

    @Override
    public WalletResult settle(String sessionId, String transactionId, String relatedTransactionId, BigDecimal amount) {
        return walletService.settle(CODE, sessionId, transactionId, relatedTransactionId, amount);
    }

    @Override
    public WalletResult rollback(String sessionId, String transactionId, String relatedTransactionId) {
        return walletService.rollback(CODE, sessionId, transactionId, relatedTransactionId);
    }
}
