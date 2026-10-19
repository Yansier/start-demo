package com.startdemo.server.web.admin.dto;

import com.startdemo.server.domain.WalletTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdminTransactionResponse(
        String transactionId,
        String uid,
        String sessionId,
        String type,
        BigDecimal amount,
        BigDecimal balanceBefore,
        BigDecimal balanceAfter,
        String relatedTransactionId,
        LocalDateTime createdAt
) {

    public static AdminTransactionResponse from(WalletTransaction transaction) {
        return new AdminTransactionResponse(
                transaction.getTransactionId(),
                transaction.getUid(),
                transaction.getSessionId(),
                transaction.getType().name(),
                transaction.getAmount(),
                transaction.getBalanceBefore(),
                transaction.getBalanceAfter(),
                transaction.getRelatedTransactionId(),
                transaction.getCreatedAt()
        );
    }
}
