package com.startdemo.server.web.admin.dto;

import java.time.LocalDateTime;

public record AdminSystemStatusResponse(
        String status,
        String service,
        String database,
        String gameProvider,
        LocalDateTime serverTime,
        String version
) {
}
