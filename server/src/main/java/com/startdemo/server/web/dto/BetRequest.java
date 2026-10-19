package com.startdemo.server.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record BetRequest(
        @NotBlank @Size(max = 64) String sessionId,
        @NotBlank @Size(max = 64) String transactionId,
        @NotNull @DecimalMin(value = "0.01", message = "金额必须大于0") BigDecimal amount
) {
}
