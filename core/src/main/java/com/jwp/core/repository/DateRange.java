package com.jwp.core.repository;

import com.jwp.core.exception.common.InvalidValueException;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 날짜 범위를 나타내는 래퍼 클래스
 * 검색 조건 등에서 시작일과 종료일을 캡슐화하여 사용합니다.
 */
@Getter
public class DateRange {

    /**
     * 시작일시
     */
    private final LocalDateTime from;

    /**
     * 종료일시
     */
    private final LocalDateTime to;

    /**
     * 날짜 범위 생성자
     *
     * @param from 시작일시
     * @param to 종료일시
     * @throws InvalidValueException 시작일시가 종료일시보다 나중인 경우
     */
    private DateRange(LocalDateTime from, LocalDateTime to) {
        validateDateRange(from, to);
        this.from = from;
        this.to = to;
    }

    /**
     * 날짜 범위 생성 팩토리 메서드
     *
     * @param from 시작일시
     * @param to 종료일시
     * @return 날짜 범위 객체
     * @throws InvalidValueException 시작일시가 종료일시보다 나중인 경우
     */
    public static DateRange of(LocalDateTime from, LocalDateTime to) {
        return new DateRange(from, to);
    }

    /**
     * 비어있는 날짜 범위 생성
     *
     * @return 비어있는 날짜 범위 객체
     */
    public static DateRange empty() {
        return new DateRange(null, null);
    }

    /**
     * 날짜 범위 유효성 검증
     *
     * @param from 시작일시
     * @param to 종료일시
     * @throws InvalidValueException 시작일시가 종료일시보다 나중인 경우
     */
    private void validateDateRange(LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new InvalidValueException("dateRange",
                    String.format("시작 일시(%s)는 종료 일시(%s)보다 이전이어야 합니다", from, to));
        }
    }

    /**
     * 날짜 범위가 비어있는지 확인
     *
     * @return 시작일시와 종료일시가 모두 null인 경우 true
     */
    public boolean isEmpty() {
        return from == null && to == null;
    }

    /**
     * 특정 일시가 날짜 범위에 포함되는지 확인
     *
     * @param dateTime 확인할 일시
     * @return 날짜 범위에 포함되면 true
     */
    public boolean contains(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }

        if (from == null && to == null) {
            return true; // 범위가 지정되지 않았으면 항상 포함
        }

        if (from == null) {
            return !dateTime.isAfter(to); // 시작일시가 없으면 종료일시 이전인지만 확인
        }

        if (to == null) {
            return !dateTime.isBefore(from); // 종료일시가 없으면 시작일시 이후인지만 확인
        }

        return !dateTime.isBefore(from) && !dateTime.isAfter(to); // 시작일시와 종료일시 사이에 있는지 확인
    }

    @Override
    public String toString() {
        return String.format("DateRange{from=%s, to=%s}", from, to);
    }
}
