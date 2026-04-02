package com.neogulss.neogulmap.db_insert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CsvInsertService3 {

  private final JdbcTemplate jdbcTemplate;

  private static final int BATCH_SIZE = 1000;

  public void insertCsv() throws IOException {

    System.out.println("CSV 읽기 시작");

    InputStream is = getClass().getResourceAsStream("/data/상권별_업종.csv");

    if (is == null) {
      throw new RuntimeException("CSV 파일을 찾을 수 없습니다.");
    }

    BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

    String line;
    boolean isFirst = true;

    List<Object[]> batch = new ArrayList<>();
    int totalCount = 0;

    while ((line = br.readLine()) != null) {

      if (isFirst) {
        isFirst = false;
        continue;
      }

      String[] data = line.split(",");

      // 컬럼 수 체크
      if (data.length < 5) {
        System.out.println("잘못된 데이터 스킵: " + line);
        continue;
      }

      try {
        batch.add(new Object[]{
            Integer.parseInt(data[0].trim()), // district_code
            data[1].trim(),                   // detail_category_name
            data[2].trim(),                   // detail_category_code (문자열 그대로)
            Integer.parseInt(data[3].trim()), // store_count
            Integer.parseInt(data[4].trim())  // quarter
        });
      } catch (Exception e) {
        System.out.println("파싱 실패 스킵: " + line);
        continue;
      }

      if (batch.size() == BATCH_SIZE) {
        insertBatch(batch);
        totalCount += batch.size();
        batch.clear();

        if (totalCount % 10000 == 0) {
          System.out.println("현재까지 삽입 수: " + totalCount);
        }
      }
    }

    // 마지막 배치 처리
    if (!batch.isEmpty()) {
      insertBatch(batch);
      totalCount += batch.size();
    }

    br.close();

    System.out.println("최종 삽입 수: " + totalCount);
  }

  private void insertBatch(List<Object[]> batch) {
    jdbcTemplate.batchUpdate(
        "INSERT INTO DISTRICT_BUSINESS " +
            "(district_code, detail_category_name, detail_category_code, store_count, quarter) " +
            "VALUES (?, ?, ?, ?, ?)",
        batch
    );
  }
}