package com.neogulss.neogulmap.db_insert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CsvInsertService2 {

  private final JdbcTemplate jdbcTemplate;

  @Transactional
  public void insertCsv() throws IOException {

    InputStream is = getClass()
        .getResourceAsStream("/data/업종_대분류.csv");

    if (is == null) {
      throw new IllegalArgumentException("CSV 파일을 찾을 수 없습니다.");
    }

    BufferedReader br = new BufferedReader(
        new InputStreamReader(is, StandardCharsets.UTF_8)
    );

    String line;
    boolean isFirst = true;

    List<Object[]> batch = new ArrayList<>();

    // 중복 제거용 Set (대분류 코드 기준)
    Set<String> uniqueCodes = new HashSet<>();

    while ((line = br.readLine()) != null) {

      // 헤더 스킵
      if (isFirst) {
        isFirst = false;
        continue;
      }

      String[] data = line.split(",");

      // 데이터 유효성 체크
      if (data.length < 2) {
        continue;
      }

      String code = data[0].trim();
      String name = data[1].trim();

      // 🔥 중복 제거 (이미 존재하면 skip)
      if (!uniqueCodes.add(code)) {
        continue;
      }

      batch.add(new Object[]{
          code,
          name
      });

      // 배치 사이즈마다 insert
      if (batch.size() == 1000) {
        insertBatch(batch);
        batch.clear();
      }
    }

    // 남은 데이터 insert
    if (!batch.isEmpty()) {
      insertBatch(batch);
    }
  }

  private void insertBatch(List<Object[]> batch) {
    jdbcTemplate.batchUpdate(
        "INSERT INTO MAIN_CATEGORY (main_category_code, main_category_name) VALUES (?, ?)",
        batch
    );
  }
}