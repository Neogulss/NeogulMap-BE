package com.neogulss.neogulmap.chatbot.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "CHAT_LOG")
public class ChatLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHAT_LOG_IDX")
    private Long chatLogIdx;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SESSION_IDX", nullable = false)
    private ChatSession session;

    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Setter
    @Column(name = "CONTENTS", columnDefinition = "json")
    private String contents;

    @OneToMany(mappedBy = "chatLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RagLog> ragLogs = new ArrayList<>();

}
