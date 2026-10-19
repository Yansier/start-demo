package com.startdemo.server.provider;

import com.startdemo.server.web.dto.WalletResult;

import java.math.BigDecimal;

public interface GameProvider {

    String getCode();

    WalletResult bet(String sessionId, String transactionId, BigDecimal amount);

    WalletResult settle(String sessionId, String transactionId, String relatedTransactionId, BigDecimal amount);

    WalletResult rollback(String sessionId, String transactionId, String relatedTransactionId);
}
