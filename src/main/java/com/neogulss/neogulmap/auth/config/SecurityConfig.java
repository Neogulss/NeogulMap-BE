package com.neogulss.neogulmap.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 설정
 * - 현재는 전체 허용으로 설정
 * - 추후 로그인 검증 적용 시 anyRequest() 부분 수정
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  /**
   * Security 필터 체인 설정
   *
   * @param http HttpSecurity
   * @return SecurityFilterChain
   * @throws Exception 설정 오류
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // CSRF 비활성화 (REST API 방식이므로)
        .csrf(csrf -> csrf.disable())

        // 요청별 인증 설정
        .authorizeHttpRequests(auth -> auth
            // 인증 없이 허용할 경로
            .requestMatchers(
                "/user/sign-up",
                "/user/login"
            ).permitAll()
            // 나머지 전체 허용 (추후 로그인 검증 적용 시 authenticated()로 변경)
            .anyRequest().permitAll()
        )

        // 세션 설정
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        );

    return http.build();
  }

  /**
   * BCryptPasswordEncoder Bean 등록
   *
   * @return BCryptPasswordEncoder
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}