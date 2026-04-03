package com.neogulss.neogulmap.report.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neogulss.neogulmap.report.config.SeoulApiProperties;
import com.neogulss.neogulmap.report.dto.SeoulApiDto.*;
import com.neogulss.neogulmap.report.feign.SeoulSangkwonClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SangkwonService {

  private static final String SVC_STORE        = "VwsmAdstrdFlpopW";
//  private static final String SVC_SALES        = "VwsmTrdhlSelngQq";
//  private static final String SVC_POPULATION   = "VwsmTrdarSelngQq";
//  private static final String SVC_WORKER       = "VwsmTrdarWrcPopltnW";
//  private static final String SVC_RESIDENT     = "VwsmTrdhlAptQq";
//  private static final String SVC_CHANGE_INDEX = "VwsmTrdarRepopQq";
  //서울시 상권분석서비스(영역-행정동) TbgisAdstrdRelmW
  //서울시 상권분석서비스(영역-자치구) TbgisSignguRelmW
  //서울시 상권분석서비스(집객시설-상권) VwsmTrdarFcltyQq
  //서울시 상권분석서비스(길단위인구-행정동) VwsmAdstrdFlpopW
  //서울시 상권분석서비스(소득소비-행정동) VwsmAdstrdNcmCnsmpW
  //서울시 상권분석서비스(아파트-행정동) VwsmAdstrdAptW
  //서울시 상권분석서비스(상권변화지표-행정동) VwsmAdstrdIxQq
  //서울시 상권분석서비스(상주인구-행정동) VwsmAdstrdRepopW
  //서울시 상권분석서비스(집객시설-행정동) VwsmAdstrdFcltyW
  private final SeoulSangkwonClient feignClient;
  private final SeoulApiProperties  props;
  private final ObjectMapper        objectMapper;

  public SangkwonReportResponse getReport(String sangkwonCode, String year, String quarter) {
    log.info("상권 리포트 조회: code={}, year={}, quarter={}", sangkwonCode, year, quarter);

    List<StoreRow>           stores      = fetch(SVC_STORE,        StoreRow.class,           sangkwonCode, year, quarter);
//    List<SalesRow>           sales       = fetch(SVC_SALES,        SalesRow.class,           sangkwonCode, year, quarter);
//    List<PopulationRow>      population  = fetch(SVC_POPULATION,   PopulationRow.class,      sangkwonCode, year, quarter);
//    List<WorkerRow>          workers     = fetch(SVC_WORKER,       WorkerRow.class,          sangkwonCode, year, quarter);
//    List<ResidentRow>        residents   = fetch(SVC_RESIDENT,     ResidentRow.class,        sangkwonCode, year, quarter);
//    List<ChangeIndicatorRow> changeIndex = fetch(SVC_CHANGE_INDEX, ChangeIndicatorRow.class, sangkwonCode, year, quarter);

    SangkwonReportResponse report = new SangkwonReportResponse();
    report.setSangkwonCode("sangkwonCode");
    report.setSangkwonName(stores.isEmpty() ? "" : stores.get(0).getSangkwonName());
    report.setYear(year);
    report.setQuarter(quarter);
    report.setStores(stores);
//    report.setSales(sales);
//    report.setPopulation(population);
//    report.setWorkers(workers);
//    report.setResidents(residents);
//    report.setChangeIndex(changeIndex);

    return report;
  }

  private <T> List<T> fetch(String service, Class<T> rowType,
      String sangkwonCode, String year, String quarter) {
    List<T> result   = new ArrayList<>();
    int     start    = 1;
    int     pageSize = props.pageSize();

    while (true) {
      String raw = feignClient.fetch(props.key(), service, start, start + pageSize - 1,year, quarter, sangkwonCode );
      log.info("=== RAW 응답 [{}] ===\n{}", service, raw);

      JsonNode response;
      try {
        response = objectMapper.readTree(raw);
      } catch (Exception e) {
        log.error("JSON 파싱 실패. 서비스: {}, 응답 원문: {}", service, raw);
        break;
      }

      // ✅ 최상위 RESULT 에러 먼저 체크 (데이터 없음, 서버 오류 등)
      JsonNode topResult = response.path("RESULT");
      if (!topResult.isMissingNode()) {
        String code = topResult.path("CODE").asText("");
        String msg  = topResult.path("MESSAGE").asText("");
        log.warn("서울시 API 최상위 에러 [{}] - {}: {}", service, code, msg);
        break;
      }

      JsonNode svcNode = response.get(service);
      if (svcNode == null) {
        log.warn("서비스 노드 없음: {}", service);
        break;
      }

      // 에러 체크 (정상: INFO-000)
      String code = svcNode.path("RESULT").path("CODE").asText("");
      if (!code.isEmpty() && !code.equals("INFO-000")) {
        String msg = svcNode.path("RESULT").path("MESSAGE").asText();
        throw new RuntimeException("서울시 API 오류 [%s]: %s".formatted(code, msg));
      }

      int      totalCount = svcNode.path("list_total_count").asInt(0);
      JsonNode rows       = svcNode.path("row");

      if (rows.isMissingNode() || !rows.isArray() || rows.isEmpty()) break;

      for (JsonNode row : rows) {
        try {
          result.add(objectMapper.treeToValue(row, rowType));
        } catch (Exception e) {
          log.error("행 파싱 실패: {}", row, e);
        }
      }

      log.debug("{} 조회: {}/{} 건", service, result.size(), totalCount);

      if (result.size() >= totalCount) break;
      start += pageSize;
    }

    return result;
  }
}