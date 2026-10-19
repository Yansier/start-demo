package com.startdemo.server.web;

import com.startdemo.server.domain.GameSession;
import com.startdemo.server.provider.GameProviderRegistry;
import com.startdemo.server.provider.MockGameProvider;
import com.startdemo.server.service.GameSessionService;
import com.startdemo.server.web.dto.LaunchRequest;
import com.startdemo.server.web.dto.SessionResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameSessionService sessionService;
    private final GameProviderRegistry providerRegistry;

    public GameController(GameSessionService sessionService, GameProviderRegistry providerRegistry) {
        this.sessionService = sessionService;
        this.providerRegistry = providerRegistry;
    }

    @PostMapping("/launch")
    public SessionResponse launch(@Valid @RequestBody LaunchRequest request) {
        String providerCode = providerRegistry.require(MockGameProvider.CODE).getCode();
        GameSession session = sessionService.launch(request.uid(), request.gameCode(), providerCode);
        return toResponse(session);
    }

    @PostMapping("/session/{sessionId}/close")
    public SessionResponse close(@PathVariable String sessionId) {
        return toResponse(sessionService.close(sessionId));
    }

    private SessionResponse toResponse(GameSession session) {
        return new SessionResponse(
                session.getSessionId(),
                session.getUid(),
                session.getGameCode(),
                session.getProvider(),
                session.getStatus().name()
        );
    }
}
