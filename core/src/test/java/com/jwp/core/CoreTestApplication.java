package com.jwp.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Core 모듈 테스트를 위한 테스트 애플리케이션 설정 클래스
 * Spring Boot 테스트에서 설정을 찾을 수 있도록 합니다.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.jwp.core")
@EnableJpaRepositories(basePackages = "com.jwp.core.repository")
public class CoreTestApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CoreTestApplication.class, args);
    }
} 