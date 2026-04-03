package com.neogulss.neogulmap.report.controller;


import com.neogulss.neogulmap.report.dto.SeoulApiDto.SangkwonReportResponse;
import com.neogulss.neogulmap.report.service.SangkwonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sangkwon")
@RequiredArgsConstructor
public class SangkwonController {

  private final SangkwonService sangkwonService;

  /**
   * 상권 분석 리포트 조회
   * <p>
   * GET /api/sangkwon/report?code=3110008&year=2024&quarter=3
   *
   * @param code    상권 코드 (SHP DBF의 TRDAR_CD)
   * @param year    기준 연도 (기본값: 2024)
   * @param quarter 기준 분기 (기본값: 3)
   */
  @GetMapping("/report")
  public ResponseEntity<SangkwonReportResponse> getReport(
      @RequestParam String code,
      @RequestParam(defaultValue = "2024") String year,
      @RequestParam(defaultValue = "3") String quarter
  ) {
    return ResponseEntity.ok(sangkwonService.getReport(code, year, quarter));
  }
}