package com.startdemo.server.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record SyncWalletRequest(
        @NotBlank @Size(max = 64) String uid,
        @NotBlank @Size(max = 64) String username,
        @NotNull @DecimalMin(value = "0.00", message = "余额不能为负数") BigDecimal balance
) {
}
