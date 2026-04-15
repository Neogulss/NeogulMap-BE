package com.neogulss.neogulmap.report.service;


import com.neogulss.neogulmap.report.dto.ReportDTO;
import com.neogulss.neogulmap.report.dto.ReportDTO.StoreRankResult;
import com.neogulss.neogulmap.report.mapper.ReportMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportMapper reportMapper;

    /**
     * 점포수, 개업, 폐업, 업종분포, 평균영업기간 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.StoreReportResponse
     */
    @Transactional
    public ReportDTO.StoreReportResponse getStoreReport(ReportDTO.Request request) {

        log.info("[{}] 점포 리포트 조회 시작 - yearQuarter: [{}]",
            request.getAdminDongCode(), request.getYearQuarter());

        ReportDTO.StoreCountResult current       = nullSafeStore(reportMapper.selectStoreCount(request));
        ReportDTO.StoreCountResult prevQuarter   = nullSafeStore(reportMapper.selectPrevQuarterStoreCount(request));
        ReportDTO.StoreCountResult prevYear      = nullSafeStore(reportMapper.selectPrevYearStoreCount(request));
        List<StoreRankResult> rankList  = reportMapper.selectDistrictStoreRank(request);
        ReportDTO.AvgOperatingResult avgMonths    = reportMapper.selectAvgOperatingMonths(request);
        ReportDTO.IndustryDistributionResult dist = reportMapper.selectIndustryDistribution(request);
        if (dist == null) dist = ReportDTO.IndustryDistributionResult.builder().build();

        // 자치구 내 등수 계산
        int rank = 1;
        for (ReportDTO.StoreRankResult row : rankList) {
            if (row.getStoreCount() > current.getStoreCount()) rank++;
        }

        // 프랜차이즈 / 일반 비율 계산
        int currentCount   = current.getStoreCount();
        int franchiseCount = current.getFranchiseStoreCount();
        int generalCount   = currentCount - franchiseCount;
        double franchiseRatio = currentCount > 0 ? Math.round((double) franchiseCount / currentCount * 1000.0) / 10.0 : 0;
        double generalRatio   = Math.round((100 - franchiseRatio) * 10.0) / 10.0;

        // 업종 비율 계산
        int totalDist       = dist.getFoodCount() + dist.getServiceCount() + dist.getRetailCount();
        double foodRatio    = totalDist > 0 ? Math.round((double) dist.getFoodCount()    / totalDist * 1000.0) / 10.0 : 0;
        double serviceRatio = totalDist > 0 ? Math.round((double) dist.getServiceCount() / totalDist * 1000.0) / 10.0 : 0;
        double retailRatio  = totalDist > 0 ? Math.round((double) dist.getRetailCount()  / totalDist * 1000.0) / 10.0 : 0;

        // 평균 영업기간 (년)
        double avgYears = Math.round(avgMonths.getAvgOperatingMonths() / 12.0 * 10.0) / 10.0;

        log.info("[{}] 점포 리포트 조회 완료 - rank: [{}]/[{}]",
            request.getAdminDongCode(), rank, rankList.size());

        return ReportDTO.StoreReportResponse.builder()
            .storeCount(currentCount)
            .prevQuarterDiff(currentCount - prevQuarter.getStoreCount())
            .prevYearDiff(currentCount - prevYear.getStoreCount())
            .rank(rank)
            .totalDongCount(rankList.size())
            .openingStoreCount(current.getOpeningStoreCount())
            .openingPrevQuarterDiff(current.getOpeningStoreCount() - prevQuarter.getOpeningStoreCount())
            .openingPrevYearDiff(current.getOpeningStoreCount() - prevYear.getOpeningStoreCount())
            .closureStoreCount(current.getClosureStoreCount())
            .closurePrevQuarterDiff(current.getClosureStoreCount() - prevQuarter.getClosureStoreCount())
            .closurePrevYearDiff(current.getClosureStoreCount() - prevYear.getClosureStoreCount())
            .franchiseStoreCount(franchiseCount)
            .generalStoreCount(generalCount)
            .franchiseRatio(franchiseRatio)
            .generalRatio(generalRatio)
            .avgOperatingMonths(avgMonths.getAvgOperatingMonths())
            .avgOperatingYears(avgYears)
            .foodCount(dist.getFoodCount())
            .serviceCount(dist.getServiceCount())
            .retailCount(dist.getRetailCount())
            .foodRatio(foodRatio)
            .serviceRatio(serviceRatio)
            .retailRatio(retailRatio)
            .build();
    }

    /**
     * 유동인구, 성별 / 연령대 / 요일 / 시간대별 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.FloatingReportResponse
     */
    @Transactional
    public ReportDTO.FloatingReportResponse getFloatingReport(ReportDTO.Request request) {

        log.info("[{}] 유동인구 리포트 조회 시작 - yearQuarter: [{}]",
            request.getAdminDongCode(), request.getYearQuarter());

        ReportDTO.FloatingPopulationResult current  = reportMapper.selectFloatingPopulation(request);
        if (current == null) current = ReportDTO.FloatingPopulationResult.builder().build();
        ReportDTO.FloatingTotalResult prevQuarter   = nullSafeFloating(reportMapper.selectPrevQuarterFloating(request));
        ReportDTO.FloatingTotalResult prevYear      = nullSafeFloating(reportMapper.selectPrevYearFloating(request));
        List<ReportDTO.FloatingRankResult> rankList = reportMapper.selectDistrictFloatingRank(request);

        // 자치구 내 등수 계산
        int rank = 1;
        for (ReportDTO.FloatingRankResult row : rankList) {
            if (row.getTotalFloatingPopulation() > current.getTotalFloatingPopulation()) rank++;
        }

        log.info("[{}] 유동인구 리포트 조회 완료 - rank: [{}]/[{}]",
            request.getAdminDongCode(), rank, rankList.size());

        return ReportDTO.FloatingReportResponse.builder()
            .totalFloatingPopulation(current.getTotalFloatingPopulation())
            .prevQuarterDiff(current.getTotalFloatingPopulation() - prevQuarter.getTotalFloatingPopulation())
            .prevYearDiff(current.getTotalFloatingPopulation() - prevYear.getTotalFloatingPopulation())
            .rank(rank)
            .totalDongCount(rankList.size())
            .maleFloatingPopulation(current.getMaleFloatingPopulation())
            .femaleFloatingPopulation(current.getFemaleFloatingPopulation())
            .age10FloatingPopulation(current.getAge10FloatingPopulation())
            .age20FloatingPopulation(current.getAge20FloatingPopulation())
            .age30FloatingPopulation(current.getAge30FloatingPopulation())
            .age40FloatingPopulation(current.getAge40FloatingPopulation())
            .age50FloatingPopulation(current.getAge50FloatingPopulation())
            .age60AboveFloatingPopulation(current.getAge60AboveFloatingPopulation())
            .time0006FloatingPopulation(current.getTime0006FloatingPopulation())
            .time0611FloatingPopulation(current.getTime0611FloatingPopulation())
            .time1114FloatingPopulation(current.getTime1114FloatingPopulation())
            .time1417FloatingPopulation(current.getTime1417FloatingPopulation())
            .time1721FloatingPopulation(current.getTime1721FloatingPopulation())
            .time2124FloatingPopulation(current.getTime2124FloatingPopulation())
            .mondayFloatingPopulation(current.getMondayFloatingPopulation())
            .tuesdayFloatingPopulation(current.getTuesdayFloatingPopulation())
            .wednesdayFloatingPopulation(current.getWednesdayFloatingPopulation())
            .thursdayFloatingPopulation(current.getThursdayFloatingPopulation())
            .fridayFloatingPopulation(current.getFridayFloatingPopulation())
            .saturdayFloatingPopulation(current.getSaturdayFloatingPopulation())
            .sundayFloatingPopulation(current.getSundayFloatingPopulation())
            .build();
    }

    /**
     * 주거인구, 성별 / 연령대별 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.ResidentReportResponse
     */
    @Transactional
    public ReportDTO.ResidentReportResponse getResidentReport(ReportDTO.Request request) {

        log.info("[{}] 주거인구 리포트 조회 시작 - yearQuarter: [{}]",
            request.getAdminDongCode(), request.getYearQuarter());

        ReportDTO.ResidentPopulationResult current = reportMapper.selectResidentPopulation(request);
        if (current == null) current = ReportDTO.ResidentPopulationResult.builder().build();
        ReportDTO.ResidentTotalResult prevQuarter  = nullSafeResident(reportMapper.selectPrevQuarterResident(request));
        ReportDTO.ResidentTotalResult prevYear     = nullSafeResident(reportMapper.selectPrevYearResident(request));

        log.info("[{}] 주거인구 리포트 조회 완료 - totalResidentPopulation: [{}]",
            request.getAdminDongCode(), current.getTotalResidentPopulation());

        return ReportDTO.ResidentReportResponse.builder()
            .totalResidentPopulation(current.getTotalResidentPopulation())
            .prevQuarterDiff(current.getTotalResidentPopulation() - prevQuarter.getTotalResidentPopulation())
            .prevYearDiff(current.getTotalResidentPopulation() - prevYear.getTotalResidentPopulation())
            .maleResidentPopulation(current.getMaleResidentPopulation())
            .femaleResidentPopulation(current.getFemaleResidentPopulation())
            .maleAge10ResidentPopulation(current.getMaleAge10ResidentPopulation())
            .maleAge20ResidentPopulation(current.getMaleAge20ResidentPopulation())
            .maleAge30ResidentPopulation(current.getMaleAge30ResidentPopulation())
            .maleAge40ResidentPopulation(current.getMaleAge40ResidentPopulation())
            .maleAge50ResidentPopulation(current.getMaleAge50ResidentPopulation())
            .maleAge60AboveResidentPopulation(current.getMaleAge60AboveResidentPopulation())
            .femaleAge10ResidentPopulation(current.getFemaleAge10ResidentPopulation())
            .femaleAge20ResidentPopulation(current.getFemaleAge20ResidentPopulation())
            .femaleAge30ResidentPopulation(current.getFemaleAge30ResidentPopulation())
            .femaleAge40ResidentPopulation(current.getFemaleAge40ResidentPopulation())
            .femaleAge50ResidentPopulation(current.getFemaleAge50ResidentPopulation())
            .femaleAge60AboveResidentPopulation(current.getFemaleAge60AboveResidentPopulation())
            .build();
    }

    /**
     * 가구세대수, 아파트 현황 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.HouseholdReportResponse
     */
    @Transactional
    public ReportDTO.HouseholdReportResponse getHouseholdReport(ReportDTO.Request request) {

        log.info("[{}] 가구세대/아파트 리포트 조회 시작 - yearQuarter: [{}]",
            request.getAdminDongCode(), request.getYearQuarter());

        ReportDTO.ResidentTotalResult current     = nullSafeResident(reportMapper.selectHousehold(request));
        ReportDTO.ResidentTotalResult prevQuarter = nullSafeResident(reportMapper.selectPrevQuarterHousehold(request));
        ReportDTO.ResidentTotalResult prevYear    = nullSafeResident(reportMapper.selectPrevYearHousehold(request));
        ReportDTO.ApartmentResult apt             = reportMapper.selectApartment(request);
        if (apt == null) apt = ReportDTO.ApartmentResult.builder().build();

        log.info("[{}] 가구세대/아파트 리포트 조회 완료 - totalHouseholdCount: [{}]",
            request.getAdminDongCode(), current.getTotalHouseholdCount());

        return ReportDTO.HouseholdReportResponse.builder()
            .totalHouseholdCount(current.getTotalHouseholdCount())
            .prevQuarterDiff(current.getTotalHouseholdCount() - prevQuarter.getTotalHouseholdCount())
            .prevYearDiff(current.getTotalHouseholdCount() - prevYear.getTotalHouseholdCount())
            .apartmentHouseholdCount(apt.getApartmentComplexCount())
            .nonApartmentHouseholdCount(apt.getApartmentComplexCount())
            .apartmentComplexCount(apt.getApartmentComplexCount())
            .aptAreaUnder66SqmUnitCount(apt.getAptAreaUnder66SqmUnitCount())
            .aptArea66SqmUnitCount(apt.getAptArea66SqmUnitCount())
            .aptArea99SqmUnitCount(apt.getAptArea99SqmUnitCount())
            .aptArea132SqmUnitCount(apt.getAptArea132SqmUnitCount())
            .aptArea165SqmUnitCount(apt.getAptArea165SqmUnitCount())
            .aptPriceUnder100mUnitCount(apt.getAptPriceUnder100mUnitCount())
            .aptPrice100mUnitCount(apt.getAptPrice100mUnitCount())
            .aptPrice200mUnitCount(apt.getAptPrice200mUnitCount())
            .aptPrice300mUnitCount(apt.getAptPrice300mUnitCount())
            .aptPrice400mUnitCount(apt.getAptPrice400mUnitCount())
            .aptPrice500mUnitCount(apt.getAptPrice500mUnitCount())
            .aptPrice600mAboveUnitCount(apt.getAptPrice600mAboveUnitCount())
            .apartmentAvgArea(apt.getApartmentAvgArea())
            .apartmentAvgPrice(apt.getApartmentAvgPrice())
            .build();
    }

    /**
     * 집객시설 현황 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.FacilityResult
     */
    @Transactional
    public ReportDTO.FacilityResult getFacilityReport(ReportDTO.Request request) {

        log.info("[{}] 집객시설 리포트 조회 시작 - yearQuarter: [{}]",
            request.getAdminDongCode(), request.getYearQuarter());

        ReportDTO.FacilityResult result = reportMapper.selectFacility(request);

        log.info("[{}] 집객시설 리포트 조회 완료 - totalVisitorFacilityCount: [{}]",
            request.getAdminDongCode(), result.getTotalVisitorFacilityCount());

        return result;
    }

    /**
     * 소득수준, 소비트렌드 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.IncomeResult
     */
    @Transactional
    public ReportDTO.IncomeResult getIncomeReport(ReportDTO.Request request) {

        log.info("[{}] 소득/소비트렌드 리포트 조회 시작 - yearQuarter: [{}]",
            request.getAdminDongCode(), request.getYearQuarter());

        ReportDTO.IncomeResult result = reportMapper.selectIncome(request);

        log.info("[{}] 소득/소비트렌드 리포트 조회 완료 - monthlyAvgIncomeAmount: [{}]",
            request.getAdminDongCode(), result.getMonthlyAvgIncomeAmount());

        return result;
    }

    /**
     * 상권변화지표 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.CommercialIndicatorResult
     */
    @Transactional
    public ReportDTO.CommercialIndicatorResult getCommercialReport(ReportDTO.Request request) {

        log.info("[{}] 상권변화지표 리포트 조회 시작 - yearQuarter: [{}]",
            request.getAdminDongCode(), request.getYearQuarter());

        ReportDTO.CommercialIndicatorResult result = reportMapper.selectCommercialIndicator(request);

        log.info("[{}] 상권변화지표 리포트 조회 완료 - indicatorName: [{}]",
            request.getAdminDongCode(), result.getCommercialChangeIndicatorName());

        return result;
    }

    // =====================================================
    // null 체크 유틸 메서드
    // =====================================================

    /**
     * StoreCountResult null 체크 - null이면 빈 객체 반환
     *
     * @param result ReportDTO.StoreCountResult
     * @return ReportDTO.StoreCountResult
     */
    private ReportDTO.StoreCountResult nullSafeStore(ReportDTO.StoreCountResult result) {
        return result != null ? result : ReportDTO.StoreCountResult.builder().build();
    }

    /**
     * FloatingTotalResult null 체크 - null이면 빈 객체 반환
     *
     * @param result ReportDTO.FloatingTotalResult
     * @return ReportDTO.FloatingTotalResult
     */
    private ReportDTO.FloatingTotalResult nullSafeFloating(ReportDTO.FloatingTotalResult result) {
        return result != null ? result : ReportDTO.FloatingTotalResult.builder().build();
    }

    /**
     * ResidentTotalResult null 체크 - null이면 빈 객체 반환
     *
     * @param result ReportDTO.ResidentTotalResult
     * @return ReportDTO.ResidentTotalResult
     */
    private ReportDTO.ResidentTotalResult nullSafeResident(ReportDTO.ResidentTotalResult result) {
        return result != null ? result : ReportDTO.ResidentTotalResult.builder().build();
    }

    /**
     * 직장인구, 성별 / 연령대별 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.WorkerReportResponse
     */
    @Transactional
    public ReportDTO.WorkerReportResponse getWorkerReport(ReportDTO.Request request) {

        log.info("[{}] 직장인구 리포트 조회 시작 - yearQuarter: [{}]",
            request.getAdminDongCode(), request.getYearQuarter());

        ReportDTO.WorkerPopulationResult current = reportMapper.selectWorkerPopulation(request);
        if (current == null) current = ReportDTO.WorkerPopulationResult.builder().build();

        ReportDTO.WorkerTotalResult prevQuarter = reportMapper.selectPrevQuarterWorker(request);
        if (prevQuarter == null) prevQuarter = ReportDTO.WorkerTotalResult.builder().build();

        ReportDTO.WorkerTotalResult prevYear = reportMapper.selectPrevYearWorker(request);
        if (prevYear == null) prevYear = ReportDTO.WorkerTotalResult.builder().build();

        log.info("[{}] 직장인구 리포트 조회 완료 - totalWorkerPopulation: [{}]",
            request.getAdminDongCode(), current.getTotalWorkerPopulation());

        return ReportDTO.WorkerReportResponse.builder()
            .totalWorkerPopulation(current.getTotalWorkerPopulation())
            .prevQuarterDiff(current.getTotalWorkerPopulation() - prevQuarter.getTotalWorkerPopulation())
            .prevYearDiff(current.getTotalWorkerPopulation() - prevYear.getTotalWorkerPopulation())
            .maleWorkerPopulation(current.getMaleWorkerPopulation())
            .femaleWorkerPopulation(current.getFemaleWorkerPopulation())
            .age10WorkerPopulation(current.getAge10WorkerPopulation())
            .age20WorkerPopulation(current.getAge20WorkerPopulation())
            .age30WorkerPopulation(current.getAge30WorkerPopulation())
            .age40WorkerPopulation(current.getAge40WorkerPopulation())
            .age50WorkerPopulation(current.getAge50WorkerPopulation())
            .age60AboveWorkerPopulation(current.getAge60AboveWorkerPopulation())
            .maleAge10WorkerPopulation(current.getMaleAge10WorkerPopulation())
            .maleAge20WorkerPopulation(current.getMaleAge20WorkerPopulation())
            .maleAge30WorkerPopulation(current.getMaleAge30WorkerPopulation())
            .maleAge40WorkerPopulation(current.getMaleAge40WorkerPopulation())
            .maleAge50WorkerPopulation(current.getMaleAge50WorkerPopulation())
            .maleAge60AboveWorkerPopulation(current.getMaleAge60AboveWorkerPopulation())
            .femaleAge10WorkerPopulation(current.getFemaleAge10WorkerPopulation())
            .femaleAge20WorkerPopulation(current.getFemaleAge20WorkerPopulation())
            .femaleAge30WorkerPopulation(current.getFemaleAge30WorkerPopulation())
            .femaleAge40WorkerPopulation(current.getFemaleAge40WorkerPopulation())
            .femaleAge50WorkerPopulation(current.getFemaleAge50WorkerPopulation())
            .femaleAge60AboveWorkerPopulation(current.getFemaleAge60AboveWorkerPopulation())
            .build();
    }

    /**
     * 추정매출, 요일 / 시간대 / 성별 / 연령대별 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.SalesReportResponse
     */
    @Transactional
    public ReportDTO.SalesReportResponse getSalesReport(ReportDTO.Request request) {

        log.info("[{}] 추정매출 리포트 조회 시작 - yearQuarter: [{}]",
            request.getAdminDongCode(), request.getYearQuarter());

        ReportDTO.EstimatedSalesResult current = reportMapper.selectEstimatedSales(request);
        if (current == null) current = ReportDTO.EstimatedSalesResult.builder().build();

        ReportDTO.SalesTotalResult prevQuarter = reportMapper.selectPrevQuarterSales(request);
        if (prevQuarter == null) prevQuarter = ReportDTO.SalesTotalResult.builder().build();

        ReportDTO.SalesTotalResult prevYear = reportMapper.selectPrevYearSales(request);
        if (prevYear == null) prevYear = ReportDTO.SalesTotalResult.builder().build();

        log.info("[{}] 추정매출 리포트 조회 완료 - monthlySalesAmount: [{}]",
            request.getAdminDongCode(), current.getMonthlySalesAmount());

        return ReportDTO.SalesReportResponse.builder()
            .monthlySalesAmount(current.getMonthlySalesAmount())
            .prevQuarterAmountDiff(current.getMonthlySalesAmount() - prevQuarter.getMonthlySalesAmount())
            .prevYearAmountDiff(current.getMonthlySalesAmount() - prevYear.getMonthlySalesAmount())
            .monthlySalesCount(current.getMonthlySalesCount())
            .prevQuarterCountDiff(current.getMonthlySalesCount() - prevQuarter.getMonthlySalesCount())
            .prevYearCountDiff(current.getMonthlySalesCount() - prevYear.getMonthlySalesCount())
            .weekdaySalesAmount(current.getWeekdaySalesAmount())
            .weekendSalesAmount(current.getWeekendSalesAmount())
            .mondaySalesAmount(current.getMondaySalesAmount())
            .tuesdaySalesAmount(current.getTuesdaySalesAmount())
            .wednesdaySalesAmount(current.getWednesdaySalesAmount())
            .thursdaySalesAmount(current.getThursdaySalesAmount())
            .fridaySalesAmount(current.getFridaySalesAmount())
            .saturdaySalesAmount(current.getSaturdaySalesAmount())
            .sundaySalesAmount(current.getSundaySalesAmount())
            .time0006SalesAmount(current.getTime0006SalesAmount())
            .time0611SalesAmount(current.getTime0611SalesAmount())
            .time1114SalesAmount(current.getTime1114SalesAmount())
            .time1417SalesAmount(current.getTime1417SalesAmount())
            .time1721SalesAmount(current.getTime1721SalesAmount())
            .time2124SalesAmount(current.getTime2124SalesAmount())
            .maleSalesAmount(current.getMaleSalesAmount())
            .femaleSalesAmount(current.getFemaleSalesAmount())
            .age10SalesAmount(current.getAge10SalesAmount())
            .age20SalesAmount(current.getAge20SalesAmount())
            .age30SalesAmount(current.getAge30SalesAmount())
            .age40SalesAmount(current.getAge40SalesAmount())
            .age50SalesAmount(current.getAge50SalesAmount())
            .age60AboveSalesAmount(current.getAge60AboveSalesAmount())
            .weekdaySalesCount(current.getWeekdaySalesCount())
            .weekendSalesCount(current.getWeekendSalesCount())
            .mondaySalesCount(current.getMondaySalesCount())
            .tuesdaySalesCount(current.getTuesdaySalesCount())
            .wednesdaySalesCount(current.getWednesdaySalesCount())
            .thursdaySalesCount(current.getThursdaySalesCount())
            .fridaySalesCount(current.getFridaySalesCount())
            .saturdaySalesCount(current.getSaturdaySalesCount())
            .sundaySalesCount(current.getSundaySalesCount())
            .time0006SalesCount(current.getTime0006SalesCount())
            .time0611SalesCount(current.getTime0611SalesCount())
            .time1114SalesCount(current.getTime1114SalesCount())
            .time1417SalesCount(current.getTime1417SalesCount())
            .time1721SalesCount(current.getTime1721SalesCount())
            .time2124SalesCount(current.getTime2124SalesCount())
            .maleSalesCount(current.getMaleSalesCount())
            .femaleSalesCount(current.getFemaleSalesCount())
            .age10SalesCount(current.getAge10SalesCount())
            .age20SalesCount(current.getAge20SalesCount())
            .age30SalesCount(current.getAge30SalesCount())
            .age40SalesCount(current.getAge40SalesCount())
            .age50SalesCount(current.getAge50SalesCount())
            .age60AboveSalesCount(current.getAge60AboveSalesCount())
            .build();
    }

    /**
     * 행정동 내 업종별 점포수 TOP5 + 월매출 TOP5 조회
     *
     * @param request ReportDTO.Request (adminDongCode, yearQuarter)
     * @return ReportDTO.TopIndustriesResponse
     */
    @Transactional
    public ReportDTO.TopIndustriesResponse getTopIndustries(ReportDTO.Request request) {
        log.info("[{}] 업종 TOP5 조회 시작 - yearQuarter: [{}]",
            request.getAdminDongCode(), request.getYearQuarter());

        List<ReportDTO.IndustryRankItem> storeTop5 = reportMapper.selectStoreTop5(request);
        List<ReportDTO.IndustryRankItem> salesTop5 = reportMapper.selectSalesTop5(request);

        return ReportDTO.TopIndustriesResponse.builder()
            .storeTop5(storeTop5)
            .salesTop5(salesTop5)
            .build();
    }

}