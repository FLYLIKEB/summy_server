package com.jwp.core.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * 테스트용 JPA 설정
 * 테스트 환경에서만 사용되는 JPA 설정입니다.
 */
@TestConfiguration
@EnableJpaRepositories(basePackages = "com.jwp.core.repository")
public class TestJpaConfig {

    /**
     * 테스트용 인메모리 데이터소스 생성
     */
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }
} 