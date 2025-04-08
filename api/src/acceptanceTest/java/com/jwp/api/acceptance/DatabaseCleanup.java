package com.jwp.api.acceptance;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 인수 테스트를 위한 데이터베이스 초기화 도우미 클래스
 * 테스트 실행 전 데이터베이스를 초기화하여 테스트 간 독립성을 보장합니다.
 */
@Component
public class DatabaseCleanup implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    /**
     * 스프링 빈 초기화 후 엔티티 테이블 이름 수집
     */
    @Override
    public void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(entityType -> entityType.getJavaType().getAnnotation(Entity.class) != null)
                .map(entityType -> {
                    Table table = entityType.getJavaType().getAnnotation(Table.class);
                    return table != null ? table.name() : entityType.getName().toLowerCase();
                })
                .collect(Collectors.toList());
    }

    /**
     * 데이터베이스의 모든 테이블 데이터 삭제
     * 테스트 실행 전 호출하여 데이터를 초기화합니다.
     */
    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1").executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
} 