package com.startdemo.server.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RollbackRequest(
        @NotBlank @Size(max = 64) String sessionId,
        @NotBlank @Size(max = 64) String transactionId,
        @NotBlank @Size(max = 64) String relatedTransactionId
) {
}
