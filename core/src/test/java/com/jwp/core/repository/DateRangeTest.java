package com.jwp.core.repository;

import com.jwp.core.exception.common.InvalidValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * DateRange 클래스 단위 테스트
 */
@DisplayName("DateRange 단위 테스트")
class DateRangeTest {

    @Test
    @DisplayName("정상적인 날짜 범위 생성")
    void of_ShouldCreateDateRange_WhenValidDates() {
        // given
        LocalDateTime from = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 12, 31, 23, 59);
        
        // when
        DateRange dateRange = DateRange.of(from, to);
        
        // then
        assertThat(dateRange).isNotNull();
        assertThat(dateRange.getFrom()).isEqualTo(from);
        assertThat(dateRange.getTo()).isEqualTo(to);
    }
    
    @Test
    @DisplayName("null 값으로도 날짜 범위 생성 가능")
    void of_ShouldCreateDateRange_WithNullValues() {
        // when
        DateRange fromOnly = DateRange.of(LocalDateTime.now(), null);
        DateRange toOnly = DateRange.of(null, LocalDateTime.now());
        DateRange empty = DateRange.empty();
        
        // then
        assertThat(fromOnly.getTo()).isNull();
        assertThat(toOnly.getFrom()).isNull();
        assertThat(empty.getFrom()).isNull();
        assertThat(empty.getTo()).isNull();
    }
    
    @Test
    @DisplayName("시작일이 종료일보다 나중이면 예외 발생")
    void of_ShouldThrowException_WhenFromIsAfterTo() {
        // given
        LocalDateTime from = LocalDateTime.of(2023, 12, 31, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 1, 1, 0, 0);
        
        // when & then
        assertThatThrownBy(() -> DateRange.of(from, to))
                .isInstanceOf(InvalidValueException.class)
                .hasMessageContaining("시작 일시");
    }
    
    @Test
    @DisplayName("날짜 범위가 비어있는지 확인")
    void isEmpty_ShouldReturnTrue_WhenBothDatesAreNull() {
        // when
        DateRange empty1 = DateRange.empty();
        DateRange empty2 = DateRange.of(null, null);
        DateRange notEmpty1 = DateRange.of(LocalDateTime.now(), null);
        DateRange notEmpty2 = DateRange.of(null, LocalDateTime.now());
        
        // then
        assertThat(empty1.isEmpty()).isTrue();
        assertThat(empty2.isEmpty()).isTrue();
        assertThat(notEmpty1.isEmpty()).isFalse();
        assertThat(notEmpty2.isEmpty()).isFalse();
    }
    
    @Test
    @DisplayName("날짜가 범위에 포함되는지 확인")
    void contains_ShouldCheckIfDateIsInRange() {
        // given
        LocalDateTime from = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 12, 31, 23, 59);
        DateRange range = DateRange.of(from, to);
        
        LocalDateTime before = LocalDateTime.of(2022, 12, 31, 23, 59);
        LocalDateTime during = LocalDateTime.of(2023, 6, 15, 12, 0);
        LocalDateTime after = LocalDateTime.of(2024, 1, 1, 0, 0);
        
        // when & then
        assertThat(range.contains(before)).isFalse();
        assertThat(range.contains(during)).isTrue();
        assertThat(range.contains(after)).isFalse();
    }
    
    @Test
    @DisplayName("시작일만 있는 경우 날짜 포함 여부 확인")
    void contains_ShouldCheckFromOnly() {
        // given
        LocalDateTime from = LocalDateTime.of(2023, 1, 1, 0, 0);
        DateRange range = DateRange.of(from, null);
        
        LocalDateTime before = LocalDateTime.of(2022, 12, 31, 23, 59);
        LocalDateTime after = LocalDateTime.of(2023, 1, 1, 0, 0);
        
        // when & then
        assertThat(range.contains(before)).isFalse();
        assertThat(range.contains(after)).isTrue();
        assertThat(range.contains(LocalDateTime.now())).isTrue(); // 현재 시간은 2023년 1월 1일 이후로 가정
    }
    
    @Test
    @DisplayName("종료일만 있는 경우 날짜 포함 여부 확인")
    void contains_ShouldCheckToOnly() {
        // given
        LocalDateTime to = LocalDateTime.of(2023, 12, 31, 23, 59);
        DateRange range = DateRange.of(null, to);
        
        LocalDateTime before = LocalDateTime.of(2023, 12, 31, 23, 59);
        LocalDateTime after = LocalDateTime.of(2024, 1, 1, 0, 0);
        
        // when & then
        assertThat(range.contains(before)).isTrue();
        assertThat(range.contains(after)).isFalse();
    }
    
    @Test
    @DisplayName("빈 날짜 범위는 모든 날짜를 포함")
    void contains_ShouldReturnTrue_WhenRangeIsEmpty() {
        // given
        DateRange empty = DateRange.empty();
        
        // when & then
        assertThat(empty.contains(LocalDateTime.now())).isTrue();
    }
    
    @Test
    @DisplayName("null 날짜는 항상 포함되지 않음")
    void contains_ShouldReturnFalse_ForNullDate() {
        // given
        DateRange range = DateRange.of(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        
        // when & then
        assertThat(range.contains(null)).isFalse();
    }
} 