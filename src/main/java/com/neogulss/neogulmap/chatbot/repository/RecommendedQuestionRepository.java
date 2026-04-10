package com.neogulss.neogulmap.chatbot.repository;

import com.neogulss.neogulmap.chatbot.entity.RecommendedQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendedQuestionRepository extends JpaRepository<RecommendedQuestion, Long> {
}

