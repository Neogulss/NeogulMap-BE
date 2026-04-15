package com.neogulss.neogulmap.report.controller;


import com.neogulss.neogulmap.common.response.BaseResponse;
import com.neogulss.neogulmap.report.dto.ReportDTO;
import com.neogulss.neogulmap.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  /**
   * 점포수, 개업, 폐업, 업종분포, 평균영업기간 조회
   *
   * @param request ReportDTO.Request
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/store")
  public ResponseEntity<BaseResponse<Object>> getStoreReport(
      @RequestBody ReportDTO.Request request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(reportService.getStoreReport(request))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 직장인구 리포트 조회
   *
   * @param request ReportDTO.Request
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/worker")
  public ResponseEntity<BaseResponse<Object>> getWorkerReport(
      @RequestBody ReportDTO.Request request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(reportService.getWorkerReport(request))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 추정매출 리포트 조회
   *
   * @param request ReportDTO.Request
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/sales")
  public ResponseEntity<BaseResponse<Object>> getSalesReport(
      @RequestBody ReportDTO.Request request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(reportService.getSalesReport(request))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 유동인구, 성별 / 연령대 / 요일 / 시간대별 조회
   *
   * @param request ReportDTO.Request
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/floating")
  public ResponseEntity<BaseResponse<Object>> getFloatingReport(
      @RequestBody ReportDTO.Request request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(reportService.getFloatingReport(request))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 주거인구, 성별 / 연령대별 조회
   *
   * @param request ReportDTO.Request
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/resident")
  public ResponseEntity<BaseResponse<Object>> getResidentReport(
      @RequestBody ReportDTO.Request request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(reportService.getResidentReport(request))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 가구세대수, 아파트 현황 조회
   *
   * @param request ReportDTO.Request
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/household")
  public ResponseEntity<BaseResponse<Object>> getHouseholdReport(
      @RequestBody ReportDTO.Request request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(reportService.getHouseholdReport(request))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 집객시설 현황 조회
   *
   * @param request ReportDTO.Request
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/facility")
  public ResponseEntity<BaseResponse<Object>> getFacilityReport(
      @RequestBody ReportDTO.Request request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(reportService.getFacilityReport(request))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 소득수준, 소비트렌드 조회
   *
   * @param request ReportDTO.Request
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/income")
  public ResponseEntity<BaseResponse<Object>> getIncomeReport(
      @RequestBody ReportDTO.Request request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(reportService.getIncomeReport(request))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 상권변화지표 조회
   *
   * @param request ReportDTO.Request
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/commercial")
  public ResponseEntity<BaseResponse<Object>> getCommercialReport(
      @RequestBody ReportDTO.Request request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(reportService.getCommercialReport(request))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 행정동 내 업종별 점포수 TOP5 + 월매출 TOP5 조회
   *
   * @param request ReportDTO.Request (adminDongCode, yearQuarter)
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/top-industries")
  public ResponseEntity<BaseResponse<Object>> getTopIndustries(
      @RequestBody ReportDTO.Request request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(reportService.getTopIndustries(request))
        .build();
    return ResponseEntity.ok(response);
  }
}