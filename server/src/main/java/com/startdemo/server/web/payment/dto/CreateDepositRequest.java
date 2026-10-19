package com.startdemo.server.web.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateDepositRequest(
        @NotBlank String uid,
        @NotBlank String channel,
        @NotNull BigDecimal amount
) {
}
