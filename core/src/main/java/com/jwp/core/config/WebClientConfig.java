package com.jwp.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient 설정 클래스
 * 외부 API 호출을 위한 WebClient 빈을 구성합니다.
 */
@Configuration
public class WebClientConfig {

    @Value("${external.api.base-url:https://api.openai.com/v1}")
    private String baseUrl;

    /**
     * WebClient 빈 생성
     * @return 설정된 WebClient 인스턴스
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }
} 