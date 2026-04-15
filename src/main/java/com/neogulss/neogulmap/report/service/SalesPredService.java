package com.neogulss.neogulmap.report.service;

import com.neogulss.neogulmap.report.client.FastApiClient;
import com.neogulss.neogulmap.report.dto.ReportDTO;
import com.neogulss.neogulmap.report.dto.SalesPredDTO;
import com.neogulss.neogulmap.report.mapper.SalesPredMapper;
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

    /**
     * 매출 예측 조회
     * DB에서 피처 조회 → FastAPI 호출 → 예측 결과 반환
     *
     * @param request ReportDTO.Request
     * @return SalesPredDTO.Response
     */
    @Transactional
    public SalesPredDTO.Response getSalesPredReport(ReportDTO.Request request) {

        log.info("[{}] 매출 예측 조회 시작 - yearQuarter: [{}], serviceIndustryCode: [{}]",
                request.getAdminDongCode(), request.getYearQuarter(), request.getServiceIndustryCode());

        // DB에서 피처 조회 (REPORT_DATA_COMMON + REPORT_DATA_SALES JOIN)
        SalesPredDTO.DbResult dbResult = salesPredMapper.selectSalesPredFeatures(request);

        if (dbResult == null) {
            log.warn("[{}] 매출 예측 피처 데이터 없음 - yearQuarter: [{}]",
                    request.getAdminDongCode(), request.getYearQuarter());
            return SalesPredDTO.Response.builder()
                    .predSales(0.0)
                    .segment(-1)
                    .confidence("LOW")
                    .message("해당 행정동/업종의 데이터가 없습니다.")
                    .build();
        }

        // DB 결과 → FastAPI 요청 변환
        SalesPredDTO.Request fastApiRequest = SalesPredDTO.Request.builder()
                .quarterCode(dbResult.getQuarterCode())
                .adminDongCode(dbResult.getAdminDongCode())
                .serviceIndustryCode(dbResult.getServiceIndustryCode())
                .salesLag1Log(dbResult.getSalesLag1Log())
                .salesLag2Log(dbResult.getSalesLag2Log())
                .salesLag3Log(dbResult.getSalesLag3Log())
                .salesLag4Log(dbResult.getSalesLag4Log())
                .salesMa2(dbResult.getSalesMa2())
                .salesMa3(dbResult.getSalesMa3())
                .salesStd2(dbResult.getSalesStd2())
                .salesStd3(dbResult.getSalesStd3())
                .salesGrowthRate(dbResult.getSalesGrowthRate())
                .salesToMa3Ratio(dbResult.getSalesToMa3Ratio())
                .salesChangeRate(dbResult.getSalesChangeRate())
                .salesToIndustryAvgRatio(dbResult.getSalesToIndustryAvgRatio())
                .operatingStoreCount(dbResult.getOperatingStoreCount())
                .totalOperatingStoreCount(dbResult.getTotalOperatingStoreCount())
                .operatingFranchiseStoreRatio(dbResult.getOperatingFranchiseStoreRatio())
                .competitionDensity(dbResult.getCompetitionDensity())
                .competitionRatio(dbResult.getCompetitionRatio())
                .areaSize(dbResult.getAreaSize())
                .floatingPopPerStore(dbResult.getFloatingPopPerStore())
                .floatingPopDensity(dbResult.getFloatingPopDensity())
                .youngPopRatio(dbResult.getYoungPopRatio())
                .weekendPopRatio(dbResult.getWeekendPopRatio())
                .floatingPopTotalLog(dbResult.getFloatingPopTotalLog())
                .avgMonthlyIncome(dbResult.getAvgMonthlyIncome())
                .foodExpenditureRatio(dbResult.getFoodExpenditureRatio())
                .entertainmentExpenditureRatio(dbResult.getEntertainmentExpenditureRatio())
                .educationExpenditureRatio(dbResult.getEducationExpenditureRatio())
                .leisureExpenditureRatio(dbResult.getLeisureExpenditureRatio())
                .totalStoreCountLag1(dbResult.getTotalStoreCountLag1())
                .totalStoreCountChange(dbResult.getTotalStoreCountChange())
                .closureRateChange(dbResult.getClosureRateChange())
                .totalPopLag1(dbResult.getTotalPopLag1())
                .floatingPopChange(dbResult.getFloatingPopChange())
                .build();

        // FastAPI 호출
        log.info("[{}] FastAPI 매출 예측 요청 - adminDongCode: [{}], serviceIndustryCode: [{}]",
                request.getAdminDongCode(), dbResult.getAdminDongCode(), dbResult.getServiceIndustryCode());

        SalesPredDTO.Response response = fastApiClient.predictSales(fastApiRequest);

        log.info("[{}] FastAPI 매출 예측 완료 - predSales: [{}], confidence: [{}]",
                request.getAdminDongCode(), response.getPredSales(), response.getConfidence());

        return response;
    }
}