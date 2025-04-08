package com.jwp.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA 관련 설정
 * JPA Auditing, 레포지토리 스캔 등의 설정을 담당합니다.
 */
@Configuration
@EnableJpaAuditing // JPA Auditing 활성화 (생성일시, 수정일시 자동 관리)
@EnableJpaRepositories(basePackages = {"com.jwp.core.repository"}) // JPA 레포지토리 스캔 경로
public class JpaConfig {
}
