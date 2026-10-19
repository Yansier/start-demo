package com.startdemo.server.web.payment.dto;

import com.startdemo.server.domain.WithdrawalOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WithdrawalOrderResponse(
        String orderNo,
        String uid,
        String channel,
        BigDecimal amount,
        BigDecimal fee,
        String address,
        String status,
        String remark,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime reviewedAt
) {

    public static WithdrawalOrderResponse from(WithdrawalOrder order) {
        return new WithdrawalOrderResponse(
                order.getOrderNo(),
                order.getUid(),
                order.getChannel().name(),
                order.getAmount(),
                order.getFee(),
                order.getAddress(),
                order.getStatus().name(),
                order.getRemark(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getReviewedAt()
        );
    }
}
