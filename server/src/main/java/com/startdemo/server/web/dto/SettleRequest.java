package com.startdemo.server.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record SettleRequest(
        @NotBlank @Size(max = 64) String sessionId,
        @NotBlank @Size(max = 64) String transactionId,
        @NotBlank @Size(max = 64) String relatedTransactionId,
        @NotNull @DecimalMin(value = "0.00", message = "金额不能为负数") BigDecimal amount
) {
}
