package com.startdemo.server.web.admin.dto;

import com.startdemo.server.domain.DemoWallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdminUserResponse(
        String uid,
        String username,
        BigDecimal balance,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static AdminUserResponse from(DemoWallet wallet) {
        return new AdminUserResponse(
                wallet.getUid(),
                wallet.getUsername(),
                wallet.getBalance(),
                wallet.getCreatedAt(),
                wallet.getUpdatedAt()
        );
    }
}
