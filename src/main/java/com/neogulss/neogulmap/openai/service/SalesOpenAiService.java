package com.neogulss.neogulmap.openai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neogulss.neogulmap.openai.client.OpenAiClient;
import com.neogulss.neogulmap.openai.dto.OpenAiClientResponseDTO;
import com.neogulss.neogulmap.openai.dto.SalesOpenAiDTO;
import com.neogulss.neogulmap.report.dto.SalesAiPromptDTO;
import com.neogulss.neogulmap.report.dto.SalesAiResponseDTO;
import com.neogulss.neogulmap.report.dto.SalesPredDTO;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesOpenAiService {

    private static final String PROMPT_TYPE = "SALES_SUMMARY";
    private static final String PROMPT_VERSION = "sales_summary_v1";

    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper;

    /**
     * 컨트롤러에서 받은 프롬프트를 그대로 OpenAI에 보내고, 화면에 보여줄 최소 응답만 만든다.
     */
    public SalesOpenAiDTO.TextResponse ask(String prompt) {
        OpenAiClientResponseDTO response = openAiClient.generate(prompt);
        return SalesOpenAiDTO.TextResponse.builder()
                .model(response.getModel())
                .text(response.getText())
                .build();
    }

    public String generateComment(SalesPredDTO.SalesInput salesInput, SalesPredDTO.SalesOutput salesOutput) {
        SalesAiResponseDTO aiResponse = generateResponse(salesInput, salesOutput);
        return aiResponse != null ? aiResponse.getText() : null;
    }

    /**
     * 매출 예측 결과를 사람이 읽을 설명으로 바꾸는 메인 업무 로직
     * 프롬프트를 조립하고 OpenAI를 호출한 뒤, DB에 저장할 응답 형태로 다시 포장한다.
     */
    public SalesAiResponseDTO generateResponse(
            SalesPredDTO.SalesInput salesInput,
            SalesPredDTO.SalesOutput salesOutput
    ) {
        try {
            String instruction = resolveInstruction(null);
            SalesAiPromptDTO promptData = buildPromptData(salesInput, salesOutput);
            OpenAiClientResponseDTO aiResponse = openAiClient.generate(instruction, toJson(promptData));
            return SalesAiResponseDTO.builder()
                    .text(aiResponse.getText())
                    .model(aiResponse.getModel())
                    .promptType(PROMPT_TYPE)
                    .promptVersion(PROMPT_VERSION)
                    .generatedAt(Instant.now().toString())
                    .build();
        } catch (Exception e) {
            Integer adminDongCode = salesOutput != null
                    ? salesOutput.getAdminDongCode()
                    : (salesInput != null ? salesInput.getAdminDongCode() : null);
            log.warn("[{}] OpenAI 매출 설명 생성 실패 - reason: {}",
                    adminDongCode, e.getMessage());
            return null;
        }
    }

    public SalesOpenAiDTO.PromptTestResponse testSalesComment(SalesOpenAiDTO.SalesCommentTestRequest request) {
        String instruction = resolveInstruction(request.getInstructionOverride());
        SalesAiPromptDTO promptData = buildPromptData(
                request.getSalesInput(),
                request.getSalesOutput()
        );

        OpenAiClientResponseDTO aiResponse = openAiClient.generate(instruction, toJson(promptData));
        return SalesOpenAiDTO.PromptTestResponse.builder()
                .model(aiResponse.getModel())
                .prompt(buildPromptPreview(instruction, promptData))
                .text(aiResponse.getText())
                .build();
    }

    private SalesAiPromptDTO buildPromptData(
            SalesPredDTO.SalesInput salesInput,
            SalesPredDTO.SalesOutput salesOutput
    ) {
        List<SalesPredDTO.TopSalesFactor> topSalesFactors = parseTopSalesFactors(
                salesOutput != null ? salesOutput.getTopSalesFactors() : null
        );
        return SalesAiPromptDTO.from(salesInput, salesOutput, topSalesFactors);
    }

    private String buildPromptPreview(String instruction, SalesAiPromptDTO promptData) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(instruction);
        if (!prompt.toString().endsWith("\n\n")) {
            prompt.append("\n\n");
        }
        prompt.append("입력 데이터(JSON):\n");
        prompt.append(toJson(promptData));
        return prompt.toString();
    }

    /**
     * 문자열을 다시 파싱해서 리스트로 역직렬화
     * SalesAiPromptDTO.java에서는 topSalesFactors 타입이 List<SalesPredDTO.TopSalesFactor>
     */
    private List<SalesPredDTO.TopSalesFactor> parseTopSalesFactors(String rawTopSalesFactors) {
        if (rawTopSalesFactors == null || rawTopSalesFactors.isBlank()) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(
                    rawTopSalesFactors,
                    new TypeReference<List<SalesPredDTO.TopSalesFactor>>() {
                    }
            );
        } catch (JsonProcessingException e) {
            log.warn("topSalesFactors 파싱 실패 - reason: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private String resolveInstruction(String instructionOverride) {
        if (instructionOverride != null && !instructionOverride.isBlank()) {
            return instructionOverride.trim();
        }

        return loadPromptTemplate();
    }

    private String loadPromptTemplate() {
        ClassPathResource resource = new ClassPathResource("data/" + PROMPT_VERSION + ".txt");
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).trim();
        } catch (IOException e) {
            throw new IllegalStateException("매출 요약 프롬프트 템플릿을 읽지 못했습니다.", e);
        }
    }

    private String toJson(SalesAiPromptDTO promptData) {
        try {
            return objectMapper.writeValueAsString(promptData);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("매출 요약 입력 JSON 직렬화에 실패했습니다.", e);
        }
    }
}
