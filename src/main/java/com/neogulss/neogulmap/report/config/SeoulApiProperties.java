package com.neogulss.neogulmap.report.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "seoul.api")

public record SeoulApiProperties(String baseUrl, String key, int pageSize) {

}
