package com.jwp.core.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Querydsl 설정
 * JPAQueryFactory를 빈으로 등록하여 프로젝트 전역에서 사용할 수 있게 합니다.
 */
@Configuration
public class QuerydslConfig {

    /** JPA 엔티티 매니저 */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * JPAQueryFactory 빈 등록
     * @return QueryDsl 쿼리 생성을 위한 JPAQueryFactory
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
} 