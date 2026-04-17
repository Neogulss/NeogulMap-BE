package com.neogulss.neogulmap.openai.dto;

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

public class SalesOpenAiDTO {

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
