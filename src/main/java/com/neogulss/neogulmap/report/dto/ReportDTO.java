package com.neogulss.neogulmap.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * 분석 리포트 DTO
 */
public class ReportDTO {

    /**
     * 분석 리포트 공통 Request
     */
    @Alias("ReportRequest")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Request {
        /** 행정동 코드 */
        private Integer adminDongCode;
        /** 기준 년분기 코드 (예: 20254 → 2025년 4분기) */
        private Integer yearQuarter;
        /** 서비스 업종 코드 (점포 관련 API에서만 사용) */
        private String serviceIndustryCode;
    }

    // =====================================================
    // 점포수 관련
    // =====================================================

    /**
     * 점포수 조회 결과
     */
    @Alias("StoreCountResult")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class StoreCountResult {
        /** 점포 수 */
        private int storeCount;
        /** 개업 점포 수 */
        private int openingStoreCount;
        /** 폐업 점포 수 */
        private int closureStoreCount;
        /** 프랜차이즈 점포 수 */
        private int franchiseStoreCount;
    }

    /**
     * 자치구 내 행정동 점포수 순위 조회 결과 (등수 계산용)
     */
    @Alias("StoreRankResult")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class StoreRankResult {
        /** 행정동 코드 */
        private int adminDongCode;
        /** 점포 수 */
        private int storeCount;
    }

    /**
     * 평균 영업기간 조회 결과
     */
    @Alias("AvgOperatingResult")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class AvgOperatingResult {
        /** 평균 영업 개월 수 */
        private double avgOperatingMonths;
    }

    /**
     * 업종분포 조회 결과 (외식업 / 서비스업 / 소매업)
     */
    @Alias("IndustryDistributionResult")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class IndustryDistributionResult {
        /** 외식업 점포 수 */
        private int foodCount;
        /** 서비스업 점포 수 */
        private int serviceCount;
        /** 소매업 점포 수 */
        private int retailCount;
    }

    // =====================================================
    // 유동인구 관련
    // =====================================================

    /**
     * 유동인구 전체 조회 결과 (성별 / 연령대 / 시간대 / 요일별 포함)
     */
    @Alias("FloatingPopulationResult")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class FloatingPopulationResult {
        /** 총 유동인구 수 */
        private long totalFloatingPopulation;
        /** 남성 유동인구 수 */
        private long maleFloatingPopulation;
        /** 여성 유동인구 수 */
        private long femaleFloatingPopulation;
        /** 10대 유동인구 수 */
        private long age10FloatingPopulation;
        /** 20대 유동인구 수 */
        private long age20FloatingPopulation;
        /** 30대 유동인구 수 */
        private long age30FloatingPopulation;
        /** 40대 유동인구 수 */
        private long age40FloatingPopulation;
        /** 50대 유동인구 수 */
        private long age50FloatingPopulation;
        /** 60대 이상 유동인구 수 */
        private long age60AboveFloatingPopulation;
        /** 00~06시 유동인구 수 */
        private long time0006FloatingPopulation;
        /** 06~11시 유동인구 수 */
        private long time0611FloatingPopulation;
        /** 11~14시 유동인구 수 */
        private long time1114FloatingPopulation;
        /** 14~17시 유동인구 수 */
        private long time1417FloatingPopulation;
        /** 17~21시 유동인구 수 */
        private long time1721FloatingPopulation;
        /** 21~24시 유동인구 수 */
        private long time2124FloatingPopulation;
        /** 월요일 유동인구 수 */
        private long mondayFloatingPopulation;
        /** 화요일 유동인구 수 */
        private long tuesdayFloatingPopulation;
        /** 수요일 유동인구 수 */
        private long wednesdayFloatingPopulation;
        /** 목요일 유동인구 수 */
        private long thursdayFloatingPopulation;
        /** 금요일 유동인구 수 */
        private long fridayFloatingPopulation;
        /** 토요일 유동인구 수 */
        private long saturdayFloatingPopulation;
        /** 일요일 유동인구 수 */
        private long sundayFloatingPopulation;
    }

    /**
     * 유동인구 합계 조회 결과 (전분기 / 전년 동분기 비교용)
     */
    @Alias("FloatingTotalResult")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class FloatingTotalResult {
        /** 총 유동인구 수 */
        private long totalFloatingPopulation;
    }

    /**
     * 자치구 내 행정동 유동인구 순위 조회 결과 (등수 계산용)
     */
    @Alias("FloatingRankResult")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class FloatingRankResult {
        /** 행정동 코드 */
        private int adminDongCode;
        /** 총 유동인구 수 */
        private long totalFloatingPopulation;
    }

    // =====================================================
    // 주거인구 관련
    // =====================================================

    /**
     * 주거인구 전체 조회 결과 (성별 / 연령대별 포함)
     */
    @Alias("ResidentPopulationResult")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ResidentPopulationResult {
        /** 총 상주인구 수 */
        private long totalResidentPopulation;
        /** 남성 상주인구 수 */
        private long maleResidentPopulation;
        /** 여성 상주인구 수 */
        private long femaleResidentPopulation;
        /** 남성 10대 상주인구 수 */
        private long maleAge10ResidentPopulation;
        /** 남성 20대 상주인구 수 */
        private long maleAge20ResidentPopulation;
        /** 남성 30대 상주인구 수 */
        private long maleAge30ResidentPopulation;
        /** 남성 40대 상주인구 수 */
        private long maleAge40ResidentPopulation;
        /** 남성 50대 상주인구 수 */
        private long maleAge50ResidentPopulation;
        /** 남성 60대 이상 상주인구 수 */
        private long maleAge60AboveResidentPopulation;
        /** 여성 10대 상주인구 수 */
        private long femaleAge10ResidentPopulation;
        /** 여성 20대 상주인구 수 */
        private long femaleAge20ResidentPopulation;
        /** 여성 30대 상주인구 수 */
        private long femaleAge30ResidentPopulation;
        /** 여성 40대 상주인구 수 */
        private long femaleAge40ResidentPopulation;
        /** 여성 50대 상주인구 수 */
        private long femaleAge50ResidentPopulation;
        /** 여성 60대 이상 상주인구 수 */
        private long femaleAge60AboveResidentPopulation;
        /** 총 가구 수 */
        private int totalHouseholdCount;
        /** 아파트 가구 수 */
        private int apartmentHouseholdCount;
        /** 비아파트 가구 수 */
        private int nonApartmentHouseholdCount;
    }

    /**
     * 주거인구 / 가구세대 합계 조회 결과 (전분기 / 전년 동분기 비교용)
     */
    @Alias("ResidentTotalResult")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ResidentTotalResult {
        /** 총 상주인구 수 */
        private long totalResidentPopulation;
        /** 총 가구 수 */
        private int totalHouseholdCount;
    }

    // =====================================================
    // 아파트 관련
    // =====================================================

    /**
     * 아파트 현황 조회 결과
     */
    @Alias("ApartmentResult")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ApartmentResult {
        /** 아파트 단지 수 */
        private int apartmentComplexCount;
        /** 66㎡ 미만 세대 수 */
        private int aptAreaUnder66SqmUnitCount;
        /** 66㎡ 세대 수 */
        private int aptArea66SqmUnitCount;
        /** 99㎡ 세대 수 */
        private int aptArea99SqmUnitCount;
        /** 132㎡ 세대 수 */
        private int aptArea132SqmUnitCount;
        /** 165㎡ 세대 수 */
        private int aptArea165SqmUnitCount;
        /** 1억 미만 세대 수 */
        private int aptPriceUnder100mUnitCount;
        /** 1억 세대 수 */
        private int aptPrice100mUnitCount;
        /** 2억 세대 수 */
        private int aptPrice200mUnitCount;
        /** 3억 세대 수 */
        private int aptPrice300mUnitCount;
        /** 4억 세대 수 */
        private int aptPrice400mUnitCount;
        /** 5억 세대 수 */
        private int aptPrice500mUnitCount;
        /** 6억 이상 세대 수 */
        private int aptPrice600mAboveUnitCount;
        /** 평균 면적 */
        private int apartmentAvgArea;
        /** 평균 시가 */
        private int apartmentAvgPrice;
    }

    // =====================================================
    // 집객시설 관련
    // =====================================================

    /**
     * 집객시설 현황 조회 결과
     */
    @Alias("FacilityResult")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class FacilityResult {
        /** 총 집객시설 수 */
        private int totalVisitorFacilityCount;
        /** 관공서 수 */
        private int governmentOfficeCount;
        /** 은행 수 */
        private int bankCount;
        /** 종합병원 수 */
        private int generalHospitalCount;
        /** 일반병원 수 */
        private int hospitalCount;
        /** 약국 수 */
        private int pharmacyCount;
        /** 유치원 수 */
        private int kindergartenCount;
        /** 초등학교 수 */
        private int elementarySchoolCount;
        /** 중학교 수 */
        private int middleSchoolCount;
        /** 고등학교 수 */
        private int highSchoolCount;
        /** 대학교 수 */
        private int universityCount;
        /** 백화점 수 */
        private int departmentStoreCount;
        /** 슈퍼마켓 수 */
        private int supermarketCount;
        /** 극장 수 */
        private int theaterCount;
        /** 숙박시설 수 */
        private int accommodationCount;
        /** 공항 수 */
        private int airportCount;
        /** 철도역 수 */
        private int railwayStationCount;
        /** 버스터미널 수 */
        private int busTerminalCount;
        /** 지하철역 수 */
        private int subwayStationCount;
        /** 버스정거장 수 */
        private int busStopCount;
    }

    // =====================================================
    // 소득 / 소비트렌드 관련
    // =====================================================

    /**
     * 소득 및 소비트렌드 조회 결과
     */
    @Alias("IncomeResult")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class IncomeResult {
        /** 월 평균 소득 금액 */
        private long monthlyAvgIncomeAmount;
        /** 소득 구간 코드 */
        private int incomeRangeCode;
        /** 지출 총금액 */
        private long totalExpenditureAmount;
        /** 식료품 지출 총금액 */
        private long foodExpenditureAmount;
        /** 의류/신발 지출 총금액 */
        private long clothingShoesExpenditureAmount;
        /** 생활용품 지출 총금액 */
        private long householdGoodsExpenditureAmount;
        /** 의료비 지출 총금액 */
        private long medicalExpenditureAmount;
        /** 교통 지출 총금액 */
        private long transportExpenditureAmount;
        /** 교육 지출 총금액 */
        private long educationExpenditureAmount;
        /** 유흥 지출 총금액 */
        private long entertainmentExpenditureAmount;
        /** 여가/문화 지출 총금액 */
        private long leisureCultureExpenditureAmount;
        /** 기타 지출 총금액 */
        private long etcExpenditureAmount;
        /** 음식(외식) 지출 총금액 */
        private long foodServiceExpenditureAmount;
    }

    // =====================================================
    // 상권변화지표 관련
    // =====================================================

    /**
     * 상권변화지표 조회 결과
     */
    @Alias("CommercialIndicatorResult")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class CommercialIndicatorResult {
        /** 상권 변화 지표 코드 */
        private String commercialChangeIndicatorCode;
        /** 상권 변화 지표명 */
        private String commercialChangeIndicatorName;
        /** 운영 영업 개월 평균 */
        private int operatingBusinessMonthAvg;
        /** 폐업 영업 개월 평균 */
        private int closedBusinessMonthAvg;
        /** 서울시 운영 영업 개월 평균 */
        private int seoulOperatingBusinessMonthAvg;
        /** 서울시 폐업 영업 개월 평균 */
        private int seoulClosedBusinessMonthAvg;
    }

    // =====================================================
    // Response DTO
    // =====================================================

    /**
     * 점포 리포트 Response
     */
    @Alias("StoreReportResponse")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class StoreReportResponse {
        /** 현재 분기 점포수 */
        private int storeCount;
        /** 전분기 대비 점포수 */
        private int prevQuarterDiff;
        /** 전년 동분기 대비 점포수 */
        private int prevYearDiff;
        /** 자치구 내 등수 */
        private int rank;
        /** 자치구 내 전체 행정동 수 */
        private int totalDongCount;
        /** 개업 점포수 */
        private int openingStoreCount;
        /** 개업 전분기 대비 */
        private int openingPrevQuarterDiff;
        /** 개업 전년 동분기 대비 */
        private int openingPrevYearDiff;
        /** 폐업 점포수 */
        private int closureStoreCount;
        /** 폐업 전분기 대비 */
        private int closurePrevQuarterDiff;
        /** 폐업 전년 동분기 대비 */
        private int closurePrevYearDiff;
        /** 프랜차이즈 점포수 */
        private int franchiseStoreCount;
        /** 일반 점포수 */
        private int generalStoreCount;
        /** 프랜차이즈 비율 (%) */
        private double franchiseRatio;
        /** 일반 비율 (%) */
        private double generalRatio;
        /** 평균 영업기간 (개월) */
        private double avgOperatingMonths;
        /** 평균 영업기간 (년) */
        private double avgOperatingYears;
        /** 외식업 점포수 */
        private int foodCount;
        /** 서비스업 점포수 */
        private int serviceCount;
        /** 소매업 점포수 */
        private int retailCount;
        /** 외식업 비율 (%) */
        private double foodRatio;
        /** 서비스업 비율 (%) */
        private double serviceRatio;
        /** 소매업 비율 (%) */
        private double retailRatio;
    }

    /**
     * 유동인구 리포트 Response
     */
    @Alias("FloatingReportResponse")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class FloatingReportResponse {
        /** 현재 분기 총 유동인구 수 */
        private long totalFloatingPopulation;
        /** 전분기 대비 유동인구 수 */
        private long prevQuarterDiff;
        /** 전년 동분기 대비 유동인구 수 */
        private long prevYearDiff;
        /** 자치구 내 등수 */
        private int rank;
        /** 자치구 내 전체 행정동 수 */
        private int totalDongCount;
        /** 남성 유동인구 수 */
        private long maleFloatingPopulation;
        /** 여성 유동인구 수 */
        private long femaleFloatingPopulation;
        /** 10대 유동인구 수 */
        private long age10FloatingPopulation;
        /** 20대 유동인구 수 */
        private long age20FloatingPopulation;
        /** 30대 유동인구 수 */
        private long age30FloatingPopulation;
        /** 40대 유동인구 수 */
        private long age40FloatingPopulation;
        /** 50대 유동인구 수 */
        private long age50FloatingPopulation;
        /** 60대 이상 유동인구 수 */
        private long age60AboveFloatingPopulation;
        /** 00~06시 유동인구 수 */
        private long time0006FloatingPopulation;
        /** 06~11시 유동인구 수 */
        private long time0611FloatingPopulation;
        /** 11~14시 유동인구 수 */
        private long time1114FloatingPopulation;
        /** 14~17시 유동인구 수 */
        private long time1417FloatingPopulation;
        /** 17~21시 유동인구 수 */
        private long time1721FloatingPopulation;
        /** 21~24시 유동인구 수 */
        private long time2124FloatingPopulation;
        /** 월요일 유동인구 수 */
        private long mondayFloatingPopulation;
        /** 화요일 유동인구 수 */
        private long tuesdayFloatingPopulation;
        /** 수요일 유동인구 수 */
        private long wednesdayFloatingPopulation;
        /** 목요일 유동인구 수 */
        private long thursdayFloatingPopulation;
        /** 금요일 유동인구 수 */
        private long fridayFloatingPopulation;
        /** 토요일 유동인구 수 */
        private long saturdayFloatingPopulation;
        /** 일요일 유동인구 수 */
        private long sundayFloatingPopulation;
    }

    /**
     * 주거인구 리포트 Response
     */
    @Alias("ResidentReportResponse")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ResidentReportResponse {
        /** 현재 분기 총 상주인구 수 */
        private long totalResidentPopulation;
        /** 전분기 대비 상주인구 수 */
        private long prevQuarterDiff;
        /** 전년 동분기 대비 상주인구 수 */
        private long prevYearDiff;
        /** 남성 상주인구 수 */
        private long maleResidentPopulation;
        /** 여성 상주인구 수 */
        private long femaleResidentPopulation;
        /** 남성 10대 상주인구 수 */
        private long maleAge10ResidentPopulation;
        /** 남성 20대 상주인구 수 */
        private long maleAge20ResidentPopulation;
        /** 남성 30대 상주인구 수 */
        private long maleAge30ResidentPopulation;
        /** 남성 40대 상주인구 수 */
        private long maleAge40ResidentPopulation;
        /** 남성 50대 상주인구 수 */
        private long maleAge50ResidentPopulation;
        /** 남성 60대 이상 상주인구 수 */
        private long maleAge60AboveResidentPopulation;
        /** 여성 10대 상주인구 수 */
        private long femaleAge10ResidentPopulation;
        /** 여성 20대 상주인구 수 */
        private long femaleAge20ResidentPopulation;
        /** 여성 30대 상주인구 수 */
        private long femaleAge30ResidentPopulation;
        /** 여성 40대 상주인구 수 */
        private long femaleAge40ResidentPopulation;
        /** 여성 50대 상주인구 수 */
        private long femaleAge50ResidentPopulation;
        /** 여성 60대 이상 상주인구 수 */
        private long femaleAge60AboveResidentPopulation;
    }

    /**
     * 가구세대 / 아파트 리포트 Response
     */
    @Alias("HouseholdReportResponse")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class HouseholdReportResponse {
        /** 총 가구 수 */
        private int totalHouseholdCount;
        /** 전분기 대비 가구 수 */
        private int prevQuarterDiff;
        /** 전년 동분기 대비 가구 수 */
        private int prevYearDiff;
        /** 아파트 가구 수 */
        private int apartmentHouseholdCount;
        /** 비아파트 가구 수 */
        private int nonApartmentHouseholdCount;
        /** 아파트 단지 수 */
        private int apartmentComplexCount;
        /** 66㎡ 미만 세대 수 */
        private int aptAreaUnder66SqmUnitCount;
        /** 66㎡ 세대 수 */
        private int aptArea66SqmUnitCount;
        /** 99㎡ 세대 수 */
        private int aptArea99SqmUnitCount;
        /** 132㎡ 세대 수 */
        private int aptArea132SqmUnitCount;
        /** 165㎡ 세대 수 */
        private int aptArea165SqmUnitCount;
        /** 1억 미만 세대 수 */
        private int aptPriceUnder100mUnitCount;
        /** 1억 세대 수 */
        private int aptPrice100mUnitCount;
        /** 2억 세대 수 */
        private int aptPrice200mUnitCount;
        /** 3억 세대 수 */
        private int aptPrice300mUnitCount;
        /** 4억 세대 수 */
        private int aptPrice400mUnitCount;
        /** 5억 세대 수 */
        private int aptPrice500mUnitCount;
        /** 6억 이상 세대 수 */
        private int aptPrice600mAboveUnitCount;
        /** 평균 면적 */
        private int apartmentAvgArea;
        /** 평균 시가 */
        private int apartmentAvgPrice;
    }

}