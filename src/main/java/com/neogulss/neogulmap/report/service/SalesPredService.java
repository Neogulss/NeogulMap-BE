package com.neogulss.neogulmap.report.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neogulss.neogulmap.report.client.FastApiClient;
import com.neogulss.neogulmap.report.dto.SalesAiResponseDTO;
import com.neogulss.neogulmap.report.dto.SalesPredDTO;
import com.neogulss.neogulmap.report.mapper.SalesPredMapper;
import com.neogulss.neogulmap.openai.service.SalesOpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SalesPredService {

    private final SalesPredMapper salesPredMapper;
    private final FastApiClient fastApiClient;
    private final ObjectMapper objectMapper;
    private final SalesOpenAiService salesOpenAiService;

    /**
     * 매출 예측 조회
     * 캐시 조회 → DB 피처 조회 → FastAPI 호출 → 예측 결과 저장/반환
     *
     * @param request SalesPredDTO.SalesUserRequest
     * @return SalesPredDTO.SalesOutput
     */
    public SalesPredDTO.SalesOutput getSalesPredReport(SalesPredDTO.SalesUserRequest request) {
        log.info("[{}] 매출 예측 조회 시작 - serviceIndustryCode: [{}]",
                request.getAdminDongCode(), request.getServiceIndustryCode());

        // 최신 feature 데이터 조회
        SalesPredDTO.SalesInput salesInput = salesPredMapper.selectSalesPredInput(request);
        if (salesInput == null) {
            log.warn("[{}] 매출 예측 피처 데이터 없음", request.getAdminDongCode());
            return SalesPredDTO.SalesOutput.builder()
                    .adminDongCode(request.getAdminDongCode())
                    .serviceIndustryCode(request.getServiceIndustryCode())
                    .segment(-1)
                    .confidence("LOW")
                    .message("해당 행정동/업종의 데이터가 없습니다.")
                    .build();
        }

        // PK 4개 기준으로 캐시 조회
        Integer baseYearQuarterCode = salesInput.getBaseYearQuarterCode();
        Integer predYearQuarterCode = toNextQuarterCode(baseYearQuarterCode);

        SalesPredDTO.SalesOutput cacheKey = SalesPredDTO.SalesOutput.builder()
                .baseYearQuarterCode(baseYearQuarterCode)
                .predYearQuarterCode(predYearQuarterCode)
                .adminDongCode(salesInput.getAdminDongCode())
                .serviceIndustryCode(salesInput.getServiceIndustryCode())
                .build();

        SalesPredDTO.SalesOutput cachedResult = salesPredMapper.selectSalesPredResult(cacheKey);
        if (cachedResult != null) {
            ensureAiComment(salesInput, cachedResult, true);
            log.info("[{}] 저장된 매출 예측 결과 반환 - baseYearQuarterCode: [{}], predYearQuarterCode: [{}]",
                    request.getAdminDongCode(), cacheKey.getBaseYearQuarterCode(), cacheKey.getPredYearQuarterCode());
            return cachedResult;
        }

        // FAST API 호출
        SalesPredDTO.SalesApiRequest apiRequest = SalesPredDTO.SalesApiRequest.from(salesInput);
        log.info("[{}] FastAPI 매출 예측 요청 - adminDongCode: [{}], serviceIndustryCode: [{}]",
                request.getAdminDongCode(), salesInput.getAdminDongCode(), salesInput.getServiceIndustryCode());

        SalesPredDTO.SalesApiResponse apiResponse = fastApiClient.predictSales(apiRequest);
        if (apiResponse == null) {
            throw new IllegalStateException("예측 서버 응답이 비어 있습니다.");
        }

        // 결과 저장
        SalesPredDTO.SalesOutput salesOutput = SalesPredDTO.SalesOutput.builder()
                .baseYearQuarterCode(cacheKey.getBaseYearQuarterCode())
                .predYearQuarterCode(cacheKey.getPredYearQuarterCode())
                .adminDongCode(salesInput.getAdminDongCode())
                .serviceIndustryCode(salesInput.getServiceIndustryCode())
                .predSalesPerStore(apiResponse.getPredSales())
                .segment(apiResponse.getSegment())
                .confidence(apiResponse.getConfidence())
                .topSalesFactors(toJson(apiResponse.getTopSalesFactors()))
                .message(apiResponse.getMessage())
                .build();

        ensureAiComment(salesInput, salesOutput, false);
        salesPredMapper.upsertSalesPredResult(salesOutput);
        SalesPredDTO.SalesOutput savedResult = salesPredMapper.selectSalesPredResult(cacheKey);

        log.info("[{}] FastAPI 매출 예측 완료 - predSales: [{}], confidence: [{}]",
                request.getAdminDongCode(), apiResponse.getPredSales(), apiResponse.getConfidence());

        SalesPredDTO.SalesOutput result = savedResult != null ? savedResult : salesOutput;
        ensureAiComment(salesInput, result, false);
        return result;
    }

    private Integer toNextQuarterCode(Integer baseYearQuarterCode) {
        if (baseYearQuarterCode == null) {
            return null;
        }

        int year = baseYearQuarterCode / 10;
        int quarter = baseYearQuarterCode % 10;
        if (quarter >= 4) {
            return ((year + 1) * 10) + 1;
        }
        return (year * 10) + (quarter + 1);
    }

//    private Long toLongValue(BigDecimal value) {
//        return value == null ? null : value.longValue();
//    }

    private String toJson(Object value) {
        try {
            return value == null ? null : objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("매출 예측 factor 직렬화에 실패했습니다.", e);
        }
    }

    private void ensureAiComment(
            SalesPredDTO.SalesInput salesInput,
            SalesPredDTO.SalesOutput salesOutput,
            boolean persistWhenGenerated
    ) {
        if (salesOutput == null) {
            return;
        }

        if (populateAiComment(salesOutput)) {
            return;
        }

        SalesAiResponseDTO aiResponse = salesOpenAiService.generateResponse(salesInput, salesOutput);
        if (aiResponse == null || aiResponse.getText() == null || aiResponse.getText().isBlank()) {
            salesOutput.setAiComment(null);
            return;
        }

        salesOutput.setAiComment(aiResponse.getText());
        salesOutput.setSalesAiResponse(toJson(aiResponse));

        if (persistWhenGenerated) {
            salesPredMapper.upsertSalesPredResult(salesOutput);
        }
    }

    private boolean populateAiComment(SalesPredDTO.SalesOutput salesOutput) {
        String salesAiResponse = salesOutput.getSalesAiResponse();
        if (salesAiResponse == null || salesAiResponse.isBlank()) {
            salesOutput.setAiComment(null);
            return false;
        }

        try {
            SalesAiResponseDTO aiResponse =
                    objectMapper.readValue(salesAiResponse, SalesAiResponseDTO.class);
            salesOutput.setAiComment(aiResponse.getText());
            return aiResponse.getText() != null && !aiResponse.getText().isBlank();
        } catch (JsonProcessingException e) {
            log.warn("[{}] sales_ai_response 파싱 실패", salesOutput.getAdminDongCode(), e);
            salesOutput.setAiComment(null);
            return false;
        }
    }
}
