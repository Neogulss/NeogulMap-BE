package com.neogulss.neogulmap.chatbot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "RAG_LOG")
public class RagLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RAG_LOG_IDX")
    private Long ragLogIdx;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHAT_LOG_IDX", nullable = false)
    private ChatLog chatLog;

    @Setter
    @Column(name = "RAG_LOG", nullable = false, columnDefinition = "json")
    private String ragLog;

    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    private LocalDateTime createdAt;

}
