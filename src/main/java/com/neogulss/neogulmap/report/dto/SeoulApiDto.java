package com.neogulss.neogulmap.report.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 서울시 상권분석 API 응답 DTO 모음
 * Lombok @Data + @NoArgsConstructor : Jackson 역직렬화를 위해 반드시 둘 다 필요
 */
public class SeoulApiDto {

  // ── 점포 현황 (VwsmTrdarStorW) ────────────────────────────────────────────
  @Data
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class StoreRow {
    @JsonProperty("STDR_YY_CD")       private String year;           // 기준 연도
    @JsonProperty("STDR_QU_CD")       private String quarter;        // 기준 분기
    @JsonProperty("TRDAR_SE_CD_NM")   private String typeName;       // 상권 구분명
    @JsonProperty("TRDAR_CD")         private String sangkwonCode;   // 상권 코드
    @JsonProperty("TRDAR_CD_NM")      private String sangkwonName;   // 상권명
    @JsonProperty("SVC_INDUTY_CD")    private String industryCode;   // 업종 코드
    @JsonProperty("SVC_INDUTY_CD_NM") private String industryName;   // 업종명
    @JsonProperty("STOR_CO")          private int storeCnt;          // 점포 수
    @JsonProperty("SIMILR_INDUTY_STOR_CO") private int similarStoreCnt; // 유사업종 점포수
    @JsonProperty("OPBIZ_RT")         private double openRate;       // 개업률
    @JsonProperty("OPBIZ_STOR_CO")    private int openStoreCnt;      // 개업 점포수
    @JsonProperty("CLSBIZ_RT")        private double closeRate;      // 폐업률
    @JsonProperty("CLSBIZ_STOR_CO")   private int closeStoreCnt;     // 폐업 점포수
    @JsonProperty("FRC_STOR_CO")      private int franchiseStoreCnt; // 프랜차이즈 점포수
  }

  // ── 추정 매출 (VwsmTrdarSelngW) ───────────────────────────────────────────
  @Data
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class SalesRow {
    @JsonProperty("STDR_YY_CD")        private String year;
    @JsonProperty("STDR_QU_CD")        private String quarter;
    @JsonProperty("TRDAR_CD")          private String sangkwonCode;
    @JsonProperty("TRDAR_CD_NM")       private String sangkwonName;
    @JsonProperty("SVC_INDUTY_CD")     private String industryCode;
    @JsonProperty("SVC_INDUTY_CD_NM")  private String industryName;
    @JsonProperty("THSMON_SELNG_AMT")  private long monthSalesAmt;    // 당월 매출액
    @JsonProperty("THSMON_SELNG_CO")   private int  monthSalesCnt;    // 당월 건수
    // 요일별 매출
    @JsonProperty("MON_SELNG_AMT")     private long monSales;
    @JsonProperty("TUE_SELNG_AMT")     private long tueSales;
    @JsonProperty("WED_SELNG_AMT")     private long wedSales;
    @JsonProperty("THU_SELNG_AMT")     private long thuSales;
    @JsonProperty("FRI_SELNG_AMT")     private long friSales;
    @JsonProperty("SAT_SELNG_AMT")     private long satSales;
    @JsonProperty("SUN_SELNG_AMT")     private long sunSales;
    // 시간대별 매출
    @JsonProperty("HR_6_9_SELNG_AMT")   private long sales6to9;
    @JsonProperty("HR_9_12_SELNG_AMT")  private long sales9to12;
    @JsonProperty("HR_12_15_SELNG_AMT") private long sales12to15;
    @JsonProperty("HR_15_18_SELNG_AMT") private long sales15to18;
    @JsonProperty("HR_18_21_SELNG_AMT") private long sales18to21;
    @JsonProperty("HR_21_24_SELNG_AMT") private long sales21to24;
    // 성별 매출
    @JsonProperty("ML_SELNG_AMT")      private long maleSalesAmt;
    @JsonProperty("FML_SELNG_AMT")     private long femaleSalesAmt;
  }

  // ── 유동인구 (VwsmTrdarFlpopW) ────────────────────────────────────────────
  @Data
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class PopulationRow {
    @JsonProperty("STDR_YY_CD")  private String year;
    @JsonProperty("STDR_QU_CD")  private String quarter;
    @JsonProperty("TRDAR_CD")    private String sangkwonCode;
    @JsonProperty("TRDAR_CD_NM") private String sangkwonName;
    @JsonProperty("TOT_FLPOP_CO")              private long total;
    @JsonProperty("ML_FLPOP_CO")               private long male;
    @JsonProperty("FML_FLPOP_CO")              private long female;
    @JsonProperty("AGRDE_10_FLPOP_CO")         private long age10s;
    @JsonProperty("AGRDE_20_FLPOP_CO")         private long age20s;
    @JsonProperty("AGRDE_30_FLPOP_CO")         private long age30s;
    @JsonProperty("AGRDE_40_FLPOP_CO")         private long age40s;
    @JsonProperty("AGRDE_50_FLPOP_CO")         private long age50s;
    @JsonProperty("AGRDE_60_ABOVE_FLPOP_CO")   private long age60plus;
    // 시간대별 유동인구
    @JsonProperty("HR_6_9_FLPOP_CO")   private long pop6to9;
    @JsonProperty("HR_9_12_FLPOP_CO")  private long pop9to12;
    @JsonProperty("HR_12_15_FLPOP_CO") private long pop12to15;
    @JsonProperty("HR_15_18_FLPOP_CO") private long pop15to18;
    @JsonProperty("HR_18_21_FLPOP_CO") private long pop18to21;
    @JsonProperty("HR_21_24_FLPOP_CO") private long pop21to24;
  }

  // ── 직장인구 (VwsmTrdarWrcPopltnW) ───────────────────────────────────────
  @Data
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class WorkerRow {
    @JsonProperty("STDR_YY_CD")  private String year;
    @JsonProperty("STDR_QU_CD")  private String quarter;
    @JsonProperty("TRDAR_CD")    private String sangkwonCode;
    @JsonProperty("TRDAR_CD_NM") private String sangkwonName;
    @JsonProperty("TOT_WRC_POPLTN_CO")              private long total;
    @JsonProperty("ML_WRC_POPLTN_CO")               private long male;
    @JsonProperty("FML_WRC_POPLTN_CO")              private long female;
    @JsonProperty("AGRDE_10_WRC_POPLTN_CO")         private long age10s;
    @JsonProperty("AGRDE_20_WRC_POPLTN_CO")         private long age20s;
    @JsonProperty("AGRDE_30_WRC_POPLTN_CO")         private long age30s;
    @JsonProperty("AGRDE_40_WRC_POPLTN_CO")         private long age40s;
    @JsonProperty("AGRDE_50_WRC_POPLTN_CO")         private long age50s;
    @JsonProperty("AGRDE_60_ABOVE_WRC_POPLTN_CO")   private long age60plus;
  }

  // ── 상주인구 (VwsmTrdarRepopW) ────────────────────────────────────────────
  @Data
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ResidentRow {
    @JsonProperty("STDR_YY_CD")  private String year;
    @JsonProperty("STDR_QU_CD")  private String quarter;
    @JsonProperty("TRDAR_CD")    private String sangkwonCode;
    @JsonProperty("TRDAR_CD_NM") private String sangkwonName;
    @JsonProperty("TOT_REPOP_CO")              private long total;
    @JsonProperty("ML_REPOP_CO")               private long male;
    @JsonProperty("FML_REPOP_CO")              private long female;
    @JsonProperty("AGRDE_10_REPOP_CO")         private long age10s;
    @JsonProperty("AGRDE_20_REPOP_CO")         private long age20s;
    @JsonProperty("AGRDE_30_REPOP_CO")         private long age30s;
    @JsonProperty("AGRDE_40_REPOP_CO")         private long age40s;
    @JsonProperty("AGRDE_50_REPOP_CO")         private long age50s;
    @JsonProperty("AGRDE_60_ABOVE_REPOP_CO")   private long age60plus;
    @JsonProperty("TOT_HSHLD_CO")              private long totalHousehold;  // 총 가구수
    @JsonProperty("APT_HSHLD_CO")              private long aptHousehold;    // 아파트 가구수
  }

  // ── 상권 변화 지표 (VwsmSignguTrdarIxW) ─────────────────────────────────
  @Data
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ChangeIndicatorRow {
    @JsonProperty("STDR_YY_CD")        private String year;
    @JsonProperty("STDR_QU_CD")        private String quarter;
    @JsonProperty("TRDAR_CD")          private String sangkwonCode;
    @JsonProperty("TRDAR_CD_NM")       private String sangkwonName;
    @JsonProperty("TRDAR_CHNGE_IX")    private String changeIndex;      // 상권 변화 지표 등급
    @JsonProperty("OPBIZ_RT")          private double openRate;
    @JsonProperty("CLSBIZ_RT")         private double closeRate;
    @JsonProperty("RUN_STOR_CHNGE_IX") private String runStoreChangeIdx; // 운영 점포 변화 지표
  }

  // ── 리포트 응답 (여러 API 조합) ───────────────────────────────────────────
  @Data
  @NoArgsConstructor
  public static class SangkwonReportResponse {
    private String sangkwonCode;
    private String sangkwonName;
    private String year;
    private String quarter;
    private java.util.List<StoreRow>          stores;
    private java.util.List<SalesRow>          sales;
    private java.util.List<PopulationRow>     population;
    private java.util.List<WorkerRow>         workers;
    private java.util.List<ResidentRow>       residents;
    private java.util.List<ChangeIndicatorRow> changeIndex;
  }
}
