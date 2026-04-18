package com.neogulss.neogulmap.report.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SalesAiPromptDTO {

    private Integer baseYearQuarterCode;
    private Integer predYearQuarterCode;
    private Integer adminDongCode;
    private String serviceIndustryCode;

    private Long predSalesPerStore;
    private String confidence;
    private Integer segment;

    private Integer quarterCode;
    private Integer operatingStoreCount;
    private Integer totalOperatingStoreCount;
    private Float competitionDensity;
    private Float competitionRatio;
    private Float floatingPopPerStore;
    private Integer avgMonthlyIncome;
    private Integer totalStoreCountChange;
    private Float salesLag1Log;
    private Float salesLag4Log;
    private Float salesMa2;
    private Float salesMa3;
    private Float salesGrowthRate;
    private Float salesToMa3Ratio;
    private Float salesToIndustryAvgRatio;
    private List<SalesPredDTO.TopSalesFactor> topSalesFactors;
    private String message;

    public static SalesAiPromptDTO from(
            SalesPredDTO.SalesInput salesInput,
            SalesPredDTO.SalesOutput salesOutput,
            List<SalesPredDTO.TopSalesFactor> topSalesFactors
    ) {
        return SalesAiPromptDTO.builder()
                .baseYearQuarterCode(salesInput != null ? salesInput.getBaseYearQuarterCode() : null)
                .predYearQuarterCode(salesOutput != null ? salesOutput.getPredYearQuarterCode() : null)
                .adminDongCode(salesOutput != null ? salesOutput.getAdminDongCode() : null)
                .serviceIndustryCode(salesOutput != null ? salesOutput.getServiceIndustryCode() : null)
                .predSalesPerStore(salesOutput != null ? salesOutput.getPredSalesPerStore() : null)
                .confidence(salesOutput != null ? salesOutput.getConfidence() : null)
                .segment(salesOutput != null ? salesOutput.getSegment() : null)
                .quarterCode(salesInput != null ? salesInput.getQuarterCode() : null)
                .operatingStoreCount(salesInput != null ? salesInput.getOperatingStoreCount() : null)
                .totalOperatingStoreCount(salesInput != null ? salesInput.getTotalOperatingStoreCount() : null)
                .competitionDensity(salesInput != null ? salesInput.getCompetitionDensity() : null)
                .competitionRatio(salesInput != null ? salesInput.getCompetitionRatio() : null)
                .floatingPopPerStore(salesInput != null ? salesInput.getFloatingPopPerStore() : null)
                .avgMonthlyIncome(salesInput != null ? salesInput.getAvgMonthlyIncome() : null)
                .totalStoreCountChange(salesInput != null ? salesInput.getTotalStoreCountChange() : null)
                .salesLag1Log(salesInput != null ? salesInput.getSalesLag1Log() : null)
                .salesLag4Log(salesInput != null ? salesInput.getSalesLag4Log() : null)
                .salesMa2(salesInput != null ? salesInput.getSalesMa2() : null)
                .salesMa3(salesInput != null ? salesInput.getSalesMa3() : null)
                .salesGrowthRate(salesInput != null ? salesInput.getSalesGrowthRate() : null)
                .salesToMa3Ratio(salesInput != null ? salesInput.getSalesToMa3Ratio() : null)
                .salesToIndustryAvgRatio(salesInput != null ? salesInput.getSalesToIndustryAvgRatio() : null)
                .topSalesFactors(topSalesFactors)
                .message(salesOutput != null ? salesOutput.getMessage() : null)
                .build();
    }
}
