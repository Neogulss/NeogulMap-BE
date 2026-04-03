package com.neogulss.neogulmap.report.config;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  /**
   * SangkwonService 에서 주입받는 ObjectMapper.
   * FAIL_ON_UNKNOWN_PROPERTIES = false : 서울시 API 응답에 알 수 없는 필드가 와도 무시
   */
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }
}
