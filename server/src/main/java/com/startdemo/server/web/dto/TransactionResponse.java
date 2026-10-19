package com.startdemo.server.web.dto;

import com.startdemo.server.domain.WalletTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        String transactionId,
        String sessionId,
        String type,
        BigDecimal amount,
        BigDecimal balanceBefore,
        BigDecimal balanceAfter,
        String relatedTransactionId,
        LocalDateTime createdAt
) {

    public static TransactionResponse from(WalletTransaction transaction) {
        return new TransactionResponse(
                transaction.getTransactionId(),
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
