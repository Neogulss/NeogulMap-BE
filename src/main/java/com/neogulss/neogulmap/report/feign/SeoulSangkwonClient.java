package com.neogulss.neogulmap.report.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 서울시 열린데이터광장 상권분석 Feign 클라이언트
 *
 * 호출 URL 패턴:
 * http://openapi.seoul.go.kr:8088/{apiKey}/json/{service}/{start}/{end}
 *
 * 서비스명 목록
 * - 점포         : VwsmTrdarStorW
 * - 추정매출     : VwsmTrdarSelngW
 * - 생활인구     : VwsmTrdarFlpopW
 * - 직장인구     : VwsmTrdarWrcPopltnW
 * - 상주인구     : VwsmTrdarRepopW
 * - 상권변화지표 : VwsmSignguTrdarIxW
 *
 * JsonNode 대신 String으로 받는 이유:
 * 서울시 API가 간혹 content-type: application/xml 로 응답해서
 * Feign의 JSON 컨버터가 거부함. String으로 받아서 직접 파싱하면 우회 가능.
 */
@FeignClient(name = "seoulApi", url = "${seoul.api.base-url}")
public interface SeoulSangkwonClient {

  @GetMapping("/{apiKey}/json/{service}/{start}/{end}")
  String fetch(  // ✅ JsonNode → String
      @PathVariable String apiKey,
      @PathVariable String service,
      @PathVariable int start,
      @PathVariable int end,
      @RequestParam("STDR_YEAR_CD") String year,
      @RequestParam("STDR_QU_CD") String quarter,
      @RequestParam("TRDAR_CD") String sangkwonCode

  );
}