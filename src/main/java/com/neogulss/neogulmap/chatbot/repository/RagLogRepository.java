package com.neogulss.neogulmap.chatbot.repository;

import com.neogulss.neogulmap.chatbot.entity.ChatLog;
import com.neogulss.neogulmap.chatbot.entity.RagLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RagLogRepository extends JpaRepository<RagLog, Long> {

    Optional<RagLog> findByChatLog(ChatLog chatLog);
}
