package com.neogulss.neogulmap.chatbot.repository;

import com.neogulss.neogulmap.chatbot.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    Optional<ChatSession> findBySessionIdxAndUserIdx(Long sessionIdx, Long userIdx);

    List<ChatSession> findByUserIdxOrderByCreatedAtDesc(Long userIdx);
}
