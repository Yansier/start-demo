package com.startdemo.server.repository;

import com.startdemo.server.domain.GameSession;
import com.startdemo.server.domain.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {

    Optional<GameSession> findBySessionId(String sessionId);

    List<GameSession> findByUid(String uid);

    List<GameSession> findTop20ByUidOrderByUpdatedAtDesc(String uid);

    long countByStatus(SessionStatus status);

    void deleteByUid(String uid);
}
