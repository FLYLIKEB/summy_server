package com.jwp.core.repository;

import com.jwp.core.domain.User;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 레포지토리 구현체
 * QueryDsl을 사용한 복잡한 쿼리 작업을 처리합니다.
 * 참고: 현재 QUser가 생성되지 않아 임시 구현입니다.
 */
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;
    
    /**
     * 생성자 (entityManager만 주입)
     * @param entityManager JPA 엔티티 매니저
     */
    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    
    /**
     * 이름에 특정 문자열이 포함된 사용자 조회 (페이징)
     * @param name 검색할 이름 (부분 일치)
     * @param pageable 페이징 정보
     * @return 페이징된 사용자 목록
     */
    @Override
    public Page<User> findByNameContaining(String name, Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("페이징 정보는 필수입니다.");
        }
        
        // JPQL로 직접 구현 (QueryDsl 대신)
        String jpql = "SELECT u FROM User u WHERE u.name LIKE :name";
        String countJpql = "SELECT COUNT(u) FROM User u WHERE u.name LIKE :name";
        
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
        
        query.setParameter("name", "%" + name + "%");
        countQuery.setParameter("name", "%" + name + "%");
        
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        
        List<User> content = query.getResultList();
        Long total = countQuery.getSingleResult();
        
        return new PageImpl<>(content, pageable, total);
    }
    
    /**
     * 조건에 맞는 사용자 검색 (페이징)
     * @param condition 검색 조건
     * @param pageable 페이징 정보
     * @return 페이징된 사용자 목록
     */
    @Override
    public Page<User> searchByCondition(UserSearchCondition condition, Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("페이징 정보는 필수입니다.");
        }
        
        // 조건이 없거나 모든 필드가 null인 경우
        if (condition == null || condition.isEmpty()) {
            return findAllUsers(pageable);
        }
        
        // JPQL 쿼리와 파라미터 생성
        StringBuilder jpql = new StringBuilder("SELECT u FROM User u WHERE 1=1");
        StringBuilder countJpql = new StringBuilder("SELECT COUNT(u) FROM User u WHERE 1=1");
        
        if (StringUtils.hasText(condition.getEmail())) {
            jpql.append(" AND u.email = :email");
            countJpql.append(" AND u.email = :email");
        }
        
        if (StringUtils.hasText(condition.getName())) {
            jpql.append(" AND u.name LIKE :name");
            countJpql.append(" AND u.name LIKE :name");
        }
        
        if (condition.getFromDate() != null) {
            jpql.append(" AND u.createdAt >= :fromDate");
            countJpql.append(" AND u.createdAt >= :fromDate");
        }
        
        if (condition.getToDate() != null) {
            jpql.append(" AND u.createdAt <= :toDate");
            countJpql.append(" AND u.createdAt <= :toDate");
        }
        
        TypedQuery<User> query = entityManager.createQuery(jpql.toString(), User.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql.toString(), Long.class);
        
        // 파라미터 설정
        if (StringUtils.hasText(condition.getEmail())) {
            query.setParameter("email", condition.getEmail());
            countQuery.setParameter("email", condition.getEmail());
        }
        
        if (StringUtils.hasText(condition.getName())) {
            query.setParameter("name", "%" + condition.getName() + "%");
            countQuery.setParameter("name", "%" + condition.getName() + "%");
        }
        
        if (condition.getFromDate() != null) {
            query.setParameter("fromDate", condition.getFromDate());
            countQuery.setParameter("fromDate", condition.getFromDate());
        }
        
        if (condition.getToDate() != null) {
            query.setParameter("toDate", condition.getToDate());
            countQuery.setParameter("toDate", condition.getToDate());
        }
        
        // 페이징 설정
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        
        List<User> content = query.getResultList();
        Long total = countQuery.getSingleResult();
        
        return new PageImpl<>(content, pageable, total);
    }
    
    /**
     * 모든 사용자 조회 (페이징)
     * @param pageable 페이징 정보
     * @return 페이징된 사용자 목록
     */
    private Page<User> findAllUsers(Pageable pageable) {
        String jpql = "SELECT u FROM User u";
        String countJpql = "SELECT COUNT(u) FROM User u";
        
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
        
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        
        List<User> content = query.getResultList();
        Long total = countQuery.getSingleResult();
        
        return new PageImpl<>(content, pageable, total);
    }
} 