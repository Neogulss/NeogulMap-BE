package com.neogulss.neogulmap.chatbot.repository;

import com.neogulss.neogulmap.chatbot.entity.ChatLog;
import com.neogulss.neogulmap.chatbot.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {

    List<ChatLog> findBySessionOrderByCreatedAtAsc(ChatSession session);
}
