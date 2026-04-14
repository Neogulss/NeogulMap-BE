package com.neogulss.neogulmap.chatbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.neogulss.neogulmap.chatbot.dto.*;
import com.neogulss.neogulmap.chatbot.entity.ChatLog;
import com.neogulss.neogulmap.chatbot.entity.ChatSession;
import com.neogulss.neogulmap.chatbot.entity.RagLog;
import com.neogulss.neogulmap.chatbot.entity.RecommendedQuestion;
import com.neogulss.neogulmap.chatbot.repository.ChatLogRepository;
import com.neogulss.neogulmap.chatbot.repository.ChatSessionRepository;
import com.neogulss.neogulmap.chatbot.repository.RagLogRepository;
import com.neogulss.neogulmap.chatbot.repository.RecommendedQuestionRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neogulss.neogulmap.chatbot.dto.ChatSessionTitleResponse;
import com.neogulss.neogulmap.chatbot.dto.DeleteChatSessionResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PolicyChatbotService {
    private static final int RECOMMENDED_LIMIT = 3;
    private static final Pattern TOKEN_PATTERN = Pattern.compile("[가-힣A-Za-z0-9]{2,}");
    private static final List<String> INITIAL_KEYWORDS = List.of(
            "서비스", "입지", "창업", "정책", "대출", "지원", "신청", "혜택"
    );

    private final PolicyChatbotAiClient policyChatbotAiClient;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatLogRepository chatLogRepository;
    private final RagLogRepository ragLogRepository;
    private final RecommendedQuestionRepository recommendedQuestionRepository;
    private final ObjectMapper objectMapper;

    public PolicyChatbotService(
            PolicyChatbotAiClient policyChatbotAiClient,
            ChatSessionRepository chatSessionRepository,
            ChatLogRepository chatLogRepository,
            RagLogRepository ragLogRepository,
            RecommendedQuestionRepository recommendedQuestionRepository,
            ObjectMapper objectMapper
    ){
        this.policyChatbotAiClient = policyChatbotAiClient;
        this.chatSessionRepository = chatSessionRepository;
        this.chatLogRepository = chatLogRepository;
        this.ragLogRepository = ragLogRepository;
        this.recommendedQuestionRepository = recommendedQuestionRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public PolicyChatbotSendResponse send(Long userIdx, PolicyChatbotSendRequest request){
        ChatSession session = resolveSession(userIdx, request.getSessionIdx());

        PolicyChatbotAiResponse aiResponse = policyChatbotAiClient.ask(request);

        if(session.getTitle() == null || session.getTitle().isBlank()){
            session.setTitle(aiResponse.getSessionTitleSuggestion());
        }

        ChatLog chatLog = new ChatLog();
        chatLog.setSession(session);
        chatLog.setContents(toJson(buildChatLogContents(aiResponse, request)));
        ChatLog savedChatLog = chatLogRepository.save(chatLog);

        RagLog ragLog =  new RagLog();
        ragLog.setChatLog(savedChatLog);
        ragLog.setRagLog(toJson(aiResponse.getRagLogPayload()));
        ragLogRepository.save(ragLog);

        return toFrontResponse(session, aiResponse);
    }

    @Transactional(readOnly = true)
    public List<ChatSessionListItemResponse> getSessions(Long userIdx){
        List<ChatSession> sessions = chatSessionRepository.findByUserIdxOrderByCreatedAtDesc(userIdx);

        List<ChatSessionListItemResponse> responses = new ArrayList<>();
        for(ChatSession session : sessions){
            ChatSessionListItemResponse item = new ChatSessionListItemResponse();
            item.setSessionIdx(session.getSessionIdx());
            item.setTitle(session.getTitle());
            item.setCreatedAt(session.getCreatedAt());
            responses.add(item);
        }

        return responses;
    }

    @Transactional(readOnly = true)
    public List<ChatLogListItemResponse> getSessionLogs(Long userIdx, Long sessionIdx){
        ChatSession session = chatSessionRepository.findBySessionIdxAndUserIdx(sessionIdx, userIdx)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않거나 접근할 수 없는 세션입니다."));

        List<ChatLog> chatLogs = chatLogRepository.findBySessionOrderByCreatedAtAsc(session);

        List<ChatLogListItemResponse> responses = new ArrayList<>();

        for(ChatLog chatLog : chatLogs){
            ChatLogListItemResponse item = new ChatLogListItemResponse();
            item.setChatLogIdx(chatLog.getChatLogIdx());
            item.setCreatedAt(chatLog.getCreatedAt());

            ChatLogContents chatContents = parseChatLogContents(chatLog.getContents());
            if (chatContents != null) {
                item.setUserQuery(chatContents.getUserQuery());
                item.setBotResponse(chatContents.getBotResponse());
                item.setModel(chatContents.getModel());
                item.setTurnLatencyMs(chatContents.getTurnLatencyMs());
            } else {
                item.setUserQuery("");
                item.setBotResponse("");
                item.setModel("");
                item.setTurnLatencyMs(0);
            }

            ragLogRepository.findByChatLog(chatLog).ifPresent(ragLog -> {
                ChatLogListItemResponse.RagLogItem ragItem = parseRagLog(ragLog.getRagLog());
                item.setRag(ragItem);
            });

            responses.add(item);
        }

        return responses;
    }

    @Transactional(readOnly = true)
    public List<RecommendedQuestionResponse> getRecommendedQuestions(Long userIdx, Long sessionIdx) {
        List<RecommendedQuestion> allQuestions = recommendedQuestionRepository.findAll();
        if (allQuestions.isEmpty()) {
            return List.of();
        }

        List<RecommendedQuestion> selected;
        if (sessionIdx == null) {
            selected = pickInitialQuestions(allQuestions, RECOMMENDED_LIMIT);
        } else {
            ChatSession session = chatSessionRepository.findBySessionIdxAndUserIdx(sessionIdx, userIdx)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 접근할 수 없는 세션입니다."));
            String latestBotResponse = extractLatestBotResponse(session);

            if (latestBotResponse == null || latestBotResponse.isBlank()) {
                selected = pickInitialQuestions(allQuestions, RECOMMENDED_LIMIT);
            } else {
                selected = pickContextualQuestions(allQuestions, latestBotResponse, RECOMMENDED_LIMIT);
            }
        }

        List<RecommendedQuestionResponse> responses = new ArrayList<>();
        for (RecommendedQuestion question : selected) {
            RecommendedQuestionResponse item = new RecommendedQuestionResponse();
            item.setQuestionIdx(question.getQuestionIdx());
            item.setQuestionTitle(question.getQuestionTitle());
            responses.add(item);
        }
        return responses;
    }

    @Transactional
    public ChatSessionTitleResponse updateSessionTitle(Long userIdx, Long sessionIdx,String title){
        ChatSession session = chatSessionRepository.findBySessionIdxAndUserIdx(sessionIdx, userIdx)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않거나 접근할 수 없는 세션입니다."));

        session.setTitle(title);

        ChatSessionTitleResponse response = new ChatSessionTitleResponse();
        response.setSessionIdx(session.getSessionIdx());
        response.setTitle(session.getTitle());
        return response;
    }

    @Transactional
    public DeleteChatSessionResponse deleteSession(Long userIdx, Long sessionIdx){
        ChatSession session = chatSessionRepository.findBySessionIdxAndUserIdx(sessionIdx, userIdx)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않거나 접근할 수 없는 세션입니다."));

        chatSessionRepository.delete(session);

        DeleteChatSessionResponse response = new DeleteChatSessionResponse();
        response.setSessionIdx(sessionIdx);
        response.setMessage("세션이 삭제되었습니다.");
        return response;
    }

    private ChatSession resolveSession(Long userIdx, Long sessionIdx){
        if(sessionIdx == null){
            ChatSession session = new ChatSession();
            session.setUserIdx(userIdx);
            session.setTitle(null);
            return chatSessionRepository.save(session);
        }

        return chatSessionRepository.findBySessionIdxAndUserIdx(sessionIdx, userIdx)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않거나 접근할 수 없는 세션입니다."));
    }

    private String extractLatestBotResponse(ChatSession session) {
        List<ChatLog> chatLogs = chatLogRepository.findBySessionOrderByCreatedAtAsc(session);
        for (int i = chatLogs.size() - 1; i >= 0; i--) {
            ChatLogContents contents = parseChatLogContents(chatLogs.get(i).getContents());
            if (contents != null && contents.getBotResponse() != null && !contents.getBotResponse().isBlank()) {
                return contents.getBotResponse();
            }
        }
        return null;
    }

    private List<RecommendedQuestion> pickInitialQuestions(List<RecommendedQuestion> questions, int limit) {
        if (questions == null || questions.isEmpty() || limit <= 0) {
            return List.of();
        }

        List<RecommendedQuestion> selected = new ArrayList<>();

        List<RecommendedQuestion> serviceCandidates = new ArrayList<>();
        List<RecommendedQuestion> others = new ArrayList<>();

        for (RecommendedQuestion question : questions) {
            if (isServiceIntroQuestion(question.getQuestionTitle())) {
                serviceCandidates.add(question);
            } else {
                others.add(question);
            }
        }

        if (!serviceCandidates.isEmpty()) {
            int pick = ThreadLocalRandom.current().nextInt(serviceCandidates.size());
            selected.add(serviceCandidates.get(pick));
        } else {
            List<RecommendedQuestion> fallback = new ArrayList<>(questions);
            Collections.shuffle(fallback);
            selected.add(fallback.get(0));
        }

        List<RecommendedQuestion> remainingPool = new ArrayList<>();
        for (RecommendedQuestion question : others) {
            if (isKeywordRelatedQuestion(question.getQuestionTitle())) {
                remainingPool.add(question);
            }
        }
        remainingPool.removeIf(item -> item.getQuestionIdx().equals(selected.get(0).getQuestionIdx()));
        Collections.shuffle(remainingPool);

        for (RecommendedQuestion question : remainingPool) {
            if (selected.size() >= limit) {
                break;
            }
            selected.add(question);
        }

        if (selected.size() < limit) {
            List<RecommendedQuestion> allShuffled = new ArrayList<>(questions);
            Collections.shuffle(allShuffled);
            for (RecommendedQuestion question : allShuffled) {
                if (selected.size() >= limit) {
                    break;
                }
                boolean exists = selected.stream()
                        .anyMatch(item -> item.getQuestionIdx().equals(question.getQuestionIdx()));
                if (!exists) {
                    selected.add(question);
                }
            }
        }

        return selected;
    }

    private boolean isServiceIntroQuestion(String questionTitle) {
        if (questionTitle == null || questionTitle.isBlank()) {
            return false;
        }

        String normalized = questionTitle.toLowerCase(Locale.ROOT);
        return normalized.contains("서비스")
                || normalized.contains("소개")
                || normalized.contains("입지너구리");
    }

    private boolean isKeywordRelatedQuestion(String questionTitle) {
        return scoreInitial(questionTitle) > 0.0;
    }

    private List<RecommendedQuestion> pickContextualQuestions(
            List<RecommendedQuestion> questions,
            String referenceText,
            int limit
    ) {
        Set<String> referenceTokens = tokenize(referenceText);
        if (referenceTokens.isEmpty()) {
            return pickInitialQuestions(questions, limit);
        }

        List<ScoredQuestion> scored = new ArrayList<>();
        for (RecommendedQuestion question : questions) {
            String title = question.getQuestionTitle();
            Set<String> questionTokens = tokenize(title);

            int overlapCount = 0;
            for (String token : questionTokens) {
                if (referenceTokens.contains(token)) {
                    overlapCount++;
                }
            }

            double score = overlapCount * 3.0 + scoreInitial(title);
            scored.add(new ScoredQuestion(question, score));
        }

        scored.sort(
                Comparator.comparingDouble(ScoredQuestion::score).reversed()
                        .thenComparing(item -> item.question().getQuestionIdx())
        );

        if (!scored.isEmpty() && scored.get(0).score() <= 0.0) {
            return pickInitialQuestions(questions, limit);
        }

        return pickTopUnique(scored, limit);
    }

    private double scoreInitial(String questionTitle) {
        if (questionTitle == null || questionTitle.isBlank()) {
            return 0.0;
        }

        String normalized = questionTitle.toLowerCase(Locale.ROOT);
        double score = 0.0;

        for (String keyword : INITIAL_KEYWORDS) {
            if (normalized.contains(keyword.toLowerCase(Locale.ROOT))) {
                score += 1.0;
            }
        }
        return score;
    }

    private Set<String> tokenize(String text) {
        Set<String> tokens = new HashSet<>();
        if (text == null || text.isBlank()) {
            return tokens;
        }

        Matcher matcher = TOKEN_PATTERN.matcher(text.toLowerCase(Locale.ROOT));
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }

    private List<RecommendedQuestion> pickTopUnique(List<ScoredQuestion> scoredQuestions, int limit) {
        List<RecommendedQuestion> selected = new ArrayList<>();
        Set<Long> usedIds = new HashSet<>();

        for (ScoredQuestion scored : scoredQuestions) {
            if (selected.size() >= limit) {
                break;
            }

            RecommendedQuestion question = scored.question();
            if (question == null || question.getQuestionIdx() == null) {
                continue;
            }
            if (usedIds.contains(question.getQuestionIdx())) {
                continue;
            }

            usedIds.add(question.getQuestionIdx());
            selected.add(question);
        }

        return selected;
    }

    private PolicyChatbotSendResponse toFrontResponse(ChatSession session, PolicyChatbotAiResponse aiResponse){
        PolicyChatbotSendResponse response = new PolicyChatbotSendResponse();
        response.setSessionIdx(session.getSessionIdx());
        response.setSessionTitle(session.getTitle());
        response.setType(aiResponse.getType());
        response.setAnswer(aiResponse.getAnswer());

        List<PolicyChatbotSendResponse.ReferenceItem> refs = new ArrayList<>();

        if (aiResponse.getRetrievedDocuments() != null) {
            for(PolicyChatbotAiResponse.RetrievedDocumentItem item : aiResponse.getRetrievedDocuments()){
                PolicyChatbotSendResponse.ReferenceItem ref = new PolicyChatbotSendResponse.ReferenceItem();
                ref.setSource(item.getSource());
                ref.setChunkText(item.getChunkText());
                ref.setFaissScore(item.getFaissScore());
                ref.setBm25Score(item.getBm25Score());
                ref.setRerankScore(item.getRerankScore());
                refs.add(ref);
            }
        }

        response.setReferences(refs);
        return response;
    }

    private ChatLogContents buildChatLogContents(
            PolicyChatbotAiResponse aiResponse,
            PolicyChatbotSendRequest request
    ) {
        ChatLogContents contents = new ChatLogContents();

        PolicyChatbotAiResponse.ChatLogPayload payload = aiResponse.getChatLogPayload();
        if (payload != null) {
            contents.setUserQuery(payload.getUserQuery());
            contents.setBotResponse(payload.getBotResponse());
            contents.setModel(payload.getModel());
            contents.setTurnLatencyMs(payload.getTurnLatencyMs());
        }

        if (request.getUserProfile() != null) {
            ChatLogUserProfile userProfile = new ChatLogUserProfile();
            userProfile.setIndustry(request.getUserProfile().getIndustry());
            userProfile.setAge(request.getUserProfile().getAge());
            userProfile.setHasBusinessRegistration(request.getUserProfile().getHasBusinessRegistration());
            userProfile.setRegion(request.getUserProfile().getRegion());
            contents.setUserProfile(userProfile);
        }

        return contents;
    }

    private String toJson(Object value){
        try{
            return objectMapper.writeValueAsString(value);
        }catch(JsonProcessingException e){
            throw new IllegalStateException("JSON 직렬화 중 오류가 발생했습니다.", e);
        }
    }

    private ChatLogContents parseChatLogContents(String json) {
        try {
            if (json == null || json.isBlank() || "null".equals(json.trim())) {
                return null;
            }
            return objectMapper.readValue(json, ChatLogContents.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("CHAT_LOG CONTENTS 파싱 중 오류가 발생했습니다.", e);
        }
    }

    private ChatLogListItemResponse.RagLogItem parseRagLog(String json){
        try{
            return objectMapper.readValue(json, ChatLogListItemResponse.RagLogItem.class);
        }catch(JsonProcessingException e){
            throw new IllegalStateException("RAG_LOG 파싱 중 오류가 발생했습니다.", e);
        }
    }

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ChatLogContents{
        private String userQuery;
        private String botResponse;
        private String model;
        private Integer turnLatencyMs;
        private ChatLogUserProfile userProfile;
    }

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ChatLogUserProfile {
        private String industry;
        private Integer age;
        private Boolean hasBusinessRegistration;
        private String region;
    }

    private record ScoredQuestion(RecommendedQuestion question, double score) {}
}
