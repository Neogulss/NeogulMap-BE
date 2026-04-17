package com.neogulss.neogulmap.openai.controller;

import com.neogulss.neogulmap.common.response.BaseResponse;
import com.neogulss.neogulmap.openai.dto.SalesOpenAiDTO;
import com.neogulss.neogulmap.openai.service.SalesOpenAiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/openai")
@RequiredArgsConstructor
public class SalesOpenAiController {

    private final SalesOpenAiService salesOpenAiService;

    @PostMapping("/test")
    public ResponseEntity<BaseResponse<Object>> testOpenAi(
            @Valid @RequestBody SalesOpenAiDTO.TestRequest request) {
        BaseResponse<Object> response = BaseResponse.builder()
                .data(salesOpenAiService.ask(request.getPrompt()))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/test/sales-comment")
    public ResponseEntity<BaseResponse<Object>> testSalesComment(
            @Valid @RequestBody SalesOpenAiDTO.SalesCommentTestRequest request) {
        BaseResponse<Object> response = BaseResponse.builder()
                .data(salesOpenAiService.testSalesComment(request))
                .build();
        return ResponseEntity.ok(response);
    }
}
