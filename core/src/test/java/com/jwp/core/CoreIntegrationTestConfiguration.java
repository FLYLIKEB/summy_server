package com.jwp.core;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Core 모듈 테스트를 위한 통합 테스트 설정
 * 레포지토리 및 엔티티 스캔을 위한 설정을 포함합니다.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.jwp.core")
@EnableJpaRepositories(basePackages = "com.jwp.core.repository")
public class CoreIntegrationTestConfiguration {
} 