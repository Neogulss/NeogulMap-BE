package com.neogulss.neogulmap.salesopenai.dto;

import com.neogulss.neogulmap.report.dto.SalesPredDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class OpenAiDTO {

    /**
     * 테스트용 요청 DTO
     * prompt: 단순 문자열로 프롬프트 받을 때 사용
     */
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class TestRequest {
        @NotBlank
        private String prompt;
    }

    /**
     * OpenAI 응답 DTO
     * model: 실제 사용된 모델명
     * text: AI가 생성한 텍스트 응답
     */
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class TextResponse {
        private String model;
        private String text;
    }

    /**
     * OpenAI Responses API 요청 DTO
     * model: 사용할 모델명
     * input: OpenAI Responses API에서 프롬프트를 받는 필드명
     */
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ResponsesRequest {
        private String model;
        private String input;
    }

    // 매출엑 테스트
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class SalesCommentTestRequest {
        private String instructionOverride;

        @Valid
        @NotNull
        private SalesPredDTO.SalesInput salesInput;

        @Valid
        @NotNull
        private SalesPredDTO.SalesOutput salesOutput;
    }

    // 매출액 테스트
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class PromptTestResponse {
        private String model;
        private String prompt;
        private String text;
    }
}
