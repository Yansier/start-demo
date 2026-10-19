package com.startdemo.server.service;

import com.startdemo.server.domain.GameSession;
import com.startdemo.server.domain.SessionStatus;
import com.startdemo.server.error.BusinessException;
import com.startdemo.server.repository.DemoWalletRepository;
import com.startdemo.server.repository.GameSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GameSessionService {

    private final GameSessionRepository sessionRepository;
    private final DemoWalletRepository walletRepository;

    public GameSessionService(GameSessionRepository sessionRepository, DemoWalletRepository walletRepository) {
        this.sessionRepository = sessionRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public GameSession launch(String uid, String gameCode, String providerCode) {
        if (walletRepository.findByUid(uid).isEmpty()) {
            throw new BusinessException("钱包不存在");
        }
        GameSession session = new GameSession(UUID.randomUUID().toString(), uid, gameCode, providerCode);
        return sessionRepository.save(session);
    }

    @Transactional
    public GameSession close(String sessionId) {
        GameSession session = require(sessionId);
        session.setStatus(SessionStatus.CLOSED);
        return sessionRepository.save(session);
    }

    @Transactional
    public void deleteByUid(String uid) {
        sessionRepository.deleteByUid(uid);
    }

    public GameSession requireActive(String sessionId, String providerCode) {
        GameSession session = requireForProvider(sessionId, providerCode);
        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new BusinessException("游戏会话已关闭");
        }
        return session;
    }

    public GameSession requireForProvider(String sessionId, String providerCode) {
        GameSession session = require(sessionId);
        if (!providerCode.equals(session.getProvider())) {
            throw new BusinessException("会话不属于当前供应商");
        }
        return session;
    }

    private GameSession require(String sessionId) {
        return sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new BusinessException("游戏会话不存在"));
    }
}
