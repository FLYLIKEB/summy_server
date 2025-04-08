package com.jwp.core;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 코어 모듈 테스트를 위한 스프링 부트 설정 클래스
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.jwp.core.repository")
@EntityScan(basePackages = "com.jwp.core.domain")
public class TestApplication {
}
