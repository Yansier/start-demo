package com.startdemo.server.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LaunchRequest(
        @NotBlank @Size(max = 64) String uid,
        @NotBlank @Size(max = 64) String gameCode
) {
}
