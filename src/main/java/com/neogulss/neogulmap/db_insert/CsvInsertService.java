package com.neogulss.neogulmap.db_insert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CsvInsertService {

  private final JdbcTemplate jdbcTemplate;

  @Transactional
  public void insertCsv() throws IOException {

    InputStream is = getClass()
        .getResourceAsStream("/data/commercial_district.csv");

    BufferedReader br = new BufferedReader(
        new InputStreamReader(is, "UTF-8"));

    String line;
    boolean isFirst = true;

    List<Object[]> batch = new ArrayList<>();

    while ((line = br.readLine()) != null) {

      if (isFirst) {
        isFirst = false;
        continue;
      }

      String[] data = line.split(",");

      batch.add(new Object[]{
          Integer.parseInt(data[0]),
          data[1],
          Integer.parseInt(data[2]),
          Double.parseDouble(data[3]),
          Double.parseDouble(data[4]),
          Integer.parseInt(data[5])
      });

      if (batch.size() == 1000) {
        insertBatch(batch);
        batch.clear();
      }
    }

    if (!batch.isEmpty()) {
      insertBatch(batch);
    }
  }

  private void insertBatch(List<Object[]> batch) {
    jdbcTemplate.batchUpdate(
        "INSERT INTO COMMERCIAL_DISTRICT VALUES (?, ?, ?, ?, ?, ?)",
        batch
    );
  }
}