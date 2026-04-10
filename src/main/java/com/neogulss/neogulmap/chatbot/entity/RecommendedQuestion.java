package com.neogulss.neogulmap.chatbot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "RECOMMENDED_QUESTION")
public class RecommendedQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTION_IDX", columnDefinition = "INT UNSIGNED")
    private Long questionIdx;

    @Column(name = "QUESTION_TITLE", nullable = false, columnDefinition = "TEXT")
    private String questionTitle;
}

