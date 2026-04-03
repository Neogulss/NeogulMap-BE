package com.neogulss.neogulmap.report.config;

import feign.RequestInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(SeoulApiProperties.class)
public class AppConfig {

  @Bean
  public RequestInterceptor jsonAcceptInterceptor() {
    return requestTemplate ->
        requestTemplate.header("Accept", "application/json");
  }
}

