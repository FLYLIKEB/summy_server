package repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static domain.QUser.user;

/**
 * QueryDsl을 사용한 사용자 레포지토리 구현체
 * 복잡한 동적 쿼리와 페이징을 처리합니다.
 */
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * QueryDsl 레포지토리 구현체 생성자
     * @param queryFactory QueryDsl 쿼리 생성을 위한 팩토리
     * @throws NullPointerException queryFactory가 null인 경우
     */
    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = Objects.requireNonNull(queryFactory, "queryFactory must not be null");
    }

    /**
     * 이름으로 사용자 검색
     * @param name 검색할 이름
     * @param pageable 페이징 정보
     * @return 검색된 사용자 목록 (페이징)
     */
    @Override
    public Page<User> findByNameContaining(String name, Pageable pageable) {
        validatePageable(pageable);
        return executeQuery(nameContains(name), pageable);
    }

    /**
     * 조건에 따른 사용자 검색
     * @param condition 검색 조건
     * @param pageable 페이징 정보
     * @return 검색된 사용자 목록 (페이징)
     */
    @Override
    public Page<User> searchByCondition(UserSearchCondition condition, Pageable pageable) {
        validatePageable(pageable);
        Objects.requireNonNull(condition, "검색 조건이 null일 수 없습니다");
        
        Predicate[] predicates = buildPredicates(condition);
        return executeQuery(predicates, pageable);
    }

    /**
     * 검색 조건으로부터 Predicate 배열 생성
     */
    private Predicate[] buildPredicates(UserSearchCondition condition) {
        return new Predicate[]{
            emailEq(condition.getEmail()),
            nameContains(condition.getName()),
            createdAtBetween(condition.getFromDate(), condition.getToDate())
        };
    }

    /**
     * 단일 조건을 배열로 변환하여 공통 실행 메서드 호출
     */
    private Page<User> executeQuery(BooleanExpression condition, Pageable pageable) {
        return executeQuery(new Predicate[]{condition}, pageable);
    }

    /**
     * 쿼리 실행 로직
     * @param predicates 검색 조건들 (null 값은 무시됨)
     * @param pageable 페이징 정보
     * @return 페이징된 검색 결과
     */
    private Page<User> executeQuery(Predicate[] predicates, Pageable pageable) {
        // 컨텐츠 조회
        List<User> content = queryFactory
                .selectFrom(user)
                .where(predicates)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 조회
        long total = queryFactory
                .selectFrom(user)
                .where(predicates)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 페이징 정보 유효성 검증
     */
    private void validatePageable(Pageable pageable) {
        Objects.requireNonNull(pageable, "페이징 정보가 null일 수 없습니다");
    }

    /**
     * 이메일 일치 조건
     */
    private BooleanExpression emailEq(String email) {
        return StringUtils.hasText(email) ? user.email.eq(email) : null;
    }

    /**
     * 이름 포함 조건
     */
    private BooleanExpression nameContains(String name) {
        return StringUtils.hasText(name) ? user.name.contains(name) : null;
    }

    /**
     * 생성일자 범위 조건
     */
    private BooleanExpression createdAtBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null || toDate == null) {
            return null;
        }
        return user.createdAt.between(fromDate, toDate);
    }
} 