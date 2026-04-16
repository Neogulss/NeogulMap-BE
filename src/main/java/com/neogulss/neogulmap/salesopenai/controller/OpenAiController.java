package com.neogulss.neogulmap.salesopenai.controller;

import com.neogulss.neogulmap.common.response.BaseResponse;
import com.neogulss.neogulmap.salesopenai.dto.OpenAiDTO;
import com.neogulss.neogulmap.salesopenai.service.OpenAiService;
import com.neogulss.neogulmap.salesopenai.service.SalesOpenAiService;
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
public class OpenAiController {

    private final OpenAiService openAiService;
    private final SalesOpenAiService salesOpenAiService;

    @PostMapping("/test")
    public ResponseEntity<BaseResponse<Object>> testOpenAi(
            @Valid @RequestBody OpenAiDTO.TestRequest request) {
        BaseResponse<Object> response = BaseResponse.builder()
                .data(openAiService.ask(request.getPrompt()))
                .build();
        return ResponseEntity.ok(response);
    }

    // 매출액 응답 테스트
    @PostMapping("/test/sales-comment")
    public ResponseEntity<BaseResponse<Object>> testSalesComment(
            @Valid @RequestBody OpenAiDTO.SalesCommentTestRequest request) {
        BaseResponse<Object> response = BaseResponse.builder()
                .data(salesOpenAiService.testSalesComment(request))
                .build();
        return ResponseEntity.ok(response);
    }
}
