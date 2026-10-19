package com.startdemo.server.web.payment.dto;

import com.startdemo.server.domain.DepositOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DepositOrderResponse(
        String orderNo,
        String uid,
        String channel,
        BigDecimal amount,
        String status,
        String remark,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime reviewedAt
) {

    public static DepositOrderResponse from(DepositOrder order) {
        return new DepositOrderResponse(
                order.getOrderNo(),
                order.getUid(),
                order.getChannel().name(),
                order.getAmount(),
                order.getStatus().name(),
                order.getRemark(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getReviewedAt()
        );
    }
}
