package com.startdemo.server.web.dto;

public record SessionResponse(
        String sessionId,
        String uid,
        String gameCode,
        String provider,
        String status
) {
}
