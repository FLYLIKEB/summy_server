package com.jwp.core.repository;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 사용자 검색 조건
 * 다양한 조건으로 사용자를 검색할 때 사용되는 조건 객체입니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSearchCondition {
    /** 검색할 이메일 */
    private String email;

    /** 검색할 이름 */
    private String name;

    /** 검색 날짜 범위 */
    private DateRange dateRange = DateRange.empty();

    /**
     * 검색 조건의 생성자
     * 롬복의 @Builder를 통해 생성하는 것을 권장합니다.
     */
    @Builder
    protected UserSearchCondition(String email, String name, DateRange dateRange) {
        this.email = email;
        this.name = name;
        this.dateRange = dateRange != null ? dateRange : DateRange.empty();
    }

    /**
     * fromDate 설정을 위한 커스텀 빌더 메서드
     * @param fromDate 검색 시작 일시
     * @return 빌더
     */
    public static UserSearchConditionBuilder fromDate(LocalDateTime fromDate) {
        return builder().dateRange(DateRange.of(fromDate, null));
    }

    /**
     * toDate 설정을 위한 커스텀 빌더 메서드
     * @param toDate 검색 종료 일시
     * @return 빌더
     */
    public static UserSearchConditionBuilder toDate(LocalDateTime toDate) {
        return builder().dateRange(DateRange.of(null, toDate));
    }

    /**
     * dateRange 설정을 위한 커스텀 빌더 메서드
     * @param fromDate 검색 시작 일시
     * @param toDate 검색 종료 일시
     * @return 빌더
     */
    public static UserSearchConditionBuilder dateRange(LocalDateTime fromDate, LocalDateTime toDate) {
        return builder().dateRange(DateRange.of(fromDate, toDate));
    }

    /**
     * 날짜 범위를 직접 지정하는 정적 팩토리 메서드
     * @param email 이메일
     * @param name 이름
     * @param fromDate 시작 일시
     * @param toDate 종료 일시
     * @return UserSearchCondition 인스턴스
     */
    public static UserSearchCondition of(String email, String name, LocalDateTime fromDate, LocalDateTime toDate) {
        return UserSearchCondition.builder()
                .email(email)
                .name(name)
                .dateRange(DateRange.of(fromDate, toDate))
                .build();
    }

    /**
     * 검색 시작 일시 getter
     * @return 검색 시작 일시
     */
    public LocalDateTime getFromDate() {
        return dateRange != null ? dateRange.getFrom() : null;
    }

    /**
     * 검색 종료 일시 getter
     * @return 검색 종료 일시
     */
    public LocalDateTime getToDate() {
        return dateRange != null ? dateRange.getTo() : null;
    }

    /**
     * 모든 필드가 비어있는지 확인
     * @return 모든 필드가 비어있는 경우 true
     */
    public boolean isEmpty() {
        return !StringUtils.hasText(this.email) &&
               !StringUtils.hasText(this.name) &&
               (dateRange == null || dateRange.isEmpty());
    }
}
