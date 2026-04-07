package com.neogulss.neogulmap;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Spring Boot 애플리케이션 컨텍스트 테스트
 * 테스트 시 DB 연결 문제 회피를 위해 H2 사용 가능
 */
@SpringBootTest
@ActiveProfiles("test") // test profile 사용 시 H2 DB 연결
class NeogulMapApplicationTests {

  @Test
  void contextLoads() {
    // 애플리케이션 컨텍스트가 정상적으로 로딩되는지 확인
  }
}
