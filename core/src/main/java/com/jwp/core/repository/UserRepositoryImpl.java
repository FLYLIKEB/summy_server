package com.jwp.core.repository;

import com.jwp.core.domain.QUser;
import com.jwp.core.domain.User;
import com.jwp.core.exception.common.InvalidValueException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 사용자 레포지토리 구현체
 * QueryDsl을 사용한 복잡한 쿼리 작업을 처리합니다.
 */
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 생성자
     *
     * @param entityManager JPA 엔티티 매니저
     */
    public UserRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 이름에 특정 문자열이 포함된 사용자 조회 (페이징)
     *
     * @param name     검색할 이름 (부분 일치)
     * @param pageable 페이징 정보
     * @return 페이징된 사용자 목록
     */
    @Override
    public Page<User> findByNameContaining(String name, Pageable pageable) {
        if (pageable == null) {
            throw new InvalidValueException("pageable", null);
        }

        // QueryDSL로 구현
        QUser user = QUser.user;

        List<User> content = queryFactory.selectFrom(user).where(user.name.contains(name)).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        Long totalCount = queryFactory.select(user.count()).from(user).where(user.name.contains(name)).fetchOne();

        return new PageImpl<>(content, pageable, totalCount != null ? totalCount : 0L);
    }

    /**
     * 조건에 맞는 사용자 검색 (페이징)
     *
     * @param condition 검색 조건
     * @param pageable  페이징 정보
     * @return 페이징된 사용자 목록
     */
    @Override
    public Page<User> searchByCondition(UserSearchCondition condition, Pageable pageable) {
        if (pageable == null) {
            throw new InvalidValueException("pageable", null);
        }

        if (condition == null || condition.isEmpty()) {
            return findAllUsers(pageable);
        }

        // QueryDSL로 구현
        QUser user = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(condition.getEmail())) {
            builder.and(user.email.eq(condition.getEmail()));
        }

        if (StringUtils.hasText(condition.getName())) {
            builder.and(user.name.contains(condition.getName()));
        }

        // 날짜 범위 조건 적용
        if (condition.getDateRange() != null && !condition.getDateRange().isEmpty()) {
            if (condition.getFromDate() != null && condition.getToDate() != null) {
                builder.and(user.createdAt.between(condition.getFromDate(), condition.getToDate()));
            } else if (condition.getFromDate() != null) {
                builder.and(user.createdAt.goe(condition.getFromDate()));
            } else if (condition.getToDate() != null) {
                builder.and(user.createdAt.loe(condition.getToDate()));
            }
        }

        List<User> content = queryFactory.selectFrom(user).where(builder).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        Long totalCount = queryFactory.select(user.count()).from(user).where(builder).fetchOne();

        return new PageImpl<>(content, pageable, totalCount != null ? totalCount : 0L);
    }

    /**
     * 모든 사용자 조회 (페이징)
     */
    private Page<User> findAllUsers(Pageable pageable) {
        QUser user = QUser.user;

        List<User> content = queryFactory.selectFrom(user).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        Long totalCount = queryFactory.select(user.count()).from(user).fetchOne();

        return new PageImpl<>(content, pageable, totalCount != null ? totalCount : 0L);
    }
}
