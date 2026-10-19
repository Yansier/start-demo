package com.startdemo.server.web.admin.dto;

import com.startdemo.server.domain.GameSession;

import java.time.LocalDateTime;

public record AdminSessionResponse(
        String sessionId,
        String gameCode,
        String provider,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static AdminSessionResponse from(GameSession session) {
        return new AdminSessionResponse(
                session.getSessionId(),
                session.getGameCode(),
                session.getProvider(),
                session.getStatus().name(),
                session.getCreatedAt(),
                session.getUpdatedAt()
        );
    }
}
