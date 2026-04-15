package com.neogulss.neogulmap.report.mapper;

import com.neogulss.neogulmap.report.dto.ReportDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ReportMapper {


    /**
     * 현재 분기 점포수 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.StoreCountResult
     */
    ReportDTO.StoreCountResult selectStoreCount(ReportDTO.Request request);

    /**
     * 전분기 점포수 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.StoreCountResult
     */
    ReportDTO.StoreCountResult selectPrevQuarterStoreCount(ReportDTO.Request request);

    /**
     * 전년 동분기 점포수 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.StoreCountResult
     */
    ReportDTO.StoreCountResult selectPrevYearStoreCount(ReportDTO.Request request);

    /**
     * 자치구 내 행정동 점포수 목록 조회 (등수 계산용)
     *
     * @param request ReportDTO.Request
     * @return List&lt;ReportDTO.StoreRankResult&gt;
     */
    List<ReportDTO.StoreRankResult> selectDistrictStoreRank(ReportDTO.Request request);

    /**
     * 평균 영업기간 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.AvgOperatingResult
     */
    ReportDTO.AvgOperatingResult selectAvgOperatingMonths(ReportDTO.Request request);

    /**
     * 업종분포 조회 (외식업 / 서비스업 / 소매업)
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.IndustryDistributionResult
     */
    ReportDTO.IndustryDistributionResult selectIndustryDistribution(ReportDTO.Request request);


    /**
     * 현재 분기 유동인구 조회 (성별 / 연령대 / 시간대 / 요일별 포함)
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.FloatingPopulationResult
     */
    ReportDTO.FloatingPopulationResult selectFloatingPopulation(ReportDTO.Request request);

    /**
     * 전분기 유동인구 합계 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.FloatingTotalResult
     */
    ReportDTO.FloatingTotalResult selectPrevQuarterFloating(ReportDTO.Request request);

    /**
     * 전년 동분기 유동인구 합계 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.FloatingTotalResult
     */
    ReportDTO.FloatingTotalResult selectPrevYearFloating(ReportDTO.Request request);

    /**
     * 자치구 내 행정동 유동인구 목록 조회 (등수 계산용)
     *
     * @param request ReportDTO.Request
     * @return List&lt;ReportDTO.FloatingRankResult&gt;
     */
    List<ReportDTO.FloatingRankResult> selectDistrictFloatingRank(ReportDTO.Request request);


    /**
     * 현재 분기 주거인구 조회 (성별 / 연령대별 포함)
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.ResidentPopulationResult
     */
    ReportDTO.ResidentPopulationResult selectResidentPopulation(ReportDTO.Request request);

    /**
     * 전분기 주거인구 합계 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.ResidentTotalResult
     */
    ReportDTO.ResidentTotalResult selectPrevQuarterResident(ReportDTO.Request request);

    /**
     * 전년 동분기 주거인구 합계 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.ResidentTotalResult
     */
    ReportDTO.ResidentTotalResult selectPrevYearResident(ReportDTO.Request request);


    /**
     * 현재 분기 가구세대 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.ResidentTotalResult
     */
    ReportDTO.ResidentTotalResult selectHousehold(ReportDTO.Request request);

    /**
     * 전분기 가구세대 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.ResidentTotalResult
     */
    ReportDTO.ResidentTotalResult selectPrevQuarterHousehold(ReportDTO.Request request);

    /**
     * 전년 동분기 가구세대 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.ResidentTotalResult
     */
    ReportDTO.ResidentTotalResult selectPrevYearHousehold(ReportDTO.Request request);

    /**
     * 아파트 현황 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.ApartmentResult
     */
    ReportDTO.ApartmentResult selectApartment(ReportDTO.Request request);



    /**
     * 집객시설 현황 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.FacilityResult
     */
    ReportDTO.FacilityResult selectFacility(ReportDTO.Request request);



    /**
     * 소득 및 소비트렌드 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.IncomeResult
     */
    ReportDTO.IncomeResult selectIncome(ReportDTO.Request request);



    /**
     * 상권변화지표 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.CommercialIndicatorResult
     */
    ReportDTO.CommercialIndicatorResult selectCommercialIndicator(ReportDTO.Request request);


    /**
     * 현재 분기 직장인구 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.WorkerPopulationResult
     */
    ReportDTO.WorkerPopulationResult selectWorkerPopulation(ReportDTO.Request request);

    /**
     * 전분기 직장인구 합계 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.WorkerTotalResult
     */
    ReportDTO.WorkerTotalResult selectPrevQuarterWorker(ReportDTO.Request request);

    /**
     * 전년 동분기 직장인구 합계 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.WorkerTotalResult
     */
    ReportDTO.WorkerTotalResult selectPrevYearWorker(ReportDTO.Request request);


    /**
     * 행정동 내 업종별 점포수 TOP5 조회
     *
     * @param request ReportDTO.Request
     * @return List&lt;ReportDTO.IndustryRankItem&gt;
     */
    List<ReportDTO.IndustryRankItem> selectStoreTop5(ReportDTO.Request request);

    /**
     * 행정동 내 업종별 월매출 TOP5 조회
     *
     * @param request ReportDTO.Request
     * @return List&lt;ReportDTO.IndustryRankItem&gt;
     */
    List<ReportDTO.IndustryRankItem> selectSalesTop5(ReportDTO.Request request);

    /**
     * 현재 분기 추정매출 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.EstimatedSalesResult
     */
    ReportDTO.EstimatedSalesResult selectEstimatedSales(ReportDTO.Request request);

    /**
     * 전분기 추정매출 합계 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.SalesTotalResult
     */
    ReportDTO.SalesTotalResult selectPrevQuarterSales(ReportDTO.Request request);

    /**
     * 전년 동분기 추정매출 합계 조회
     *
     * @param request ReportDTO.Request
     * @return ReportDTO.SalesTotalResult
     */
    ReportDTO.SalesTotalResult selectPrevYearSales(ReportDTO.Request request);
}