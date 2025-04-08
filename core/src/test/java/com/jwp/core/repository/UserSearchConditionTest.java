package com.jwp.core.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jwp.core.exception.common.InvalidValueException;

@DisplayName("UserSearchCondition 테스트")
class UserSearchConditionTest {
    
    @Test
    @DisplayName("빌더로 검색 조건 생성")
    void builder_ShouldCreateCondition_WithAllFields() {
        // given
        String email = "test@example.com";
        String name = "테스트";
        LocalDateTime fromDate = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime toDate = LocalDateTime.of(2022, 12, 31, 23, 59);
        
        // when
        UserSearchCondition condition = UserSearchCondition.builder()
                .email(email)
                .name(name)
                .fromDate(fromDate)
                .toDate(toDate)
                .build();
        
        // then
        assertThat(condition.getEmail()).isEqualTo(email);
        assertThat(condition.getName()).isEqualTo(name);
        assertThat(condition.getFromDate()).isEqualTo(fromDate);
        assertThat(condition.getToDate()).isEqualTo(toDate);
        assertThat(condition.isEmpty()).isFalse();
    }
    
    @Test
    @DisplayName("dateRange 메소드로 날짜 범위 설정")
    void dateRange_ShouldSetBothDates() {
        // given
        LocalDateTime fromDate = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime toDate = LocalDateTime.of(2022, 12, 31, 23, 59);
        
        // when
        UserSearchCondition condition = UserSearchCondition.builder()
                .dateRange(fromDate, toDate)
                .build();
        
        // then
        assertThat(condition.getFromDate()).isEqualTo(fromDate);
        assertThat(condition.getToDate()).isEqualTo(toDate);
    }
    
    @Test
    @DisplayName("잘못된 날짜 범위 설정 시 예외 발생")
    void dateRange_ShouldThrowException_WhenInvalidRange() {
        // given
        LocalDateTime fromDate = LocalDateTime.of(2022, 12, 31, 23, 59);
        LocalDateTime toDate = LocalDateTime.of(2022, 1, 1, 0, 0);
        
        // when & then
        assertThatThrownBy(() -> UserSearchCondition.builder().dateRange(fromDate, toDate))
                .isInstanceOf(InvalidValueException.class)
                .hasMessageContaining("시작 일시");
    }
    
    @Test
    @DisplayName("모든 필드가 null일 때 isEmpty는 true 반환")
    void isEmpty_ShouldReturnTrue_WhenAllFieldsAreNull() {
        // given & when
        UserSearchCondition condition = UserSearchCondition.builder().build();
        
        // then
        assertThat(condition.isEmpty()).isTrue();
    }
    
    @Test
    @DisplayName("하나라도 필드가 null이 아닐 때 isEmpty는 false 반환")
    void isEmpty_ShouldReturnFalse_WhenAnyFieldIsNotNull() {
        // given & when
        UserSearchCondition emailOnly = UserSearchCondition.builder().email("test@example.com").build();
        UserSearchCondition nameOnly = UserSearchCondition.builder().name("테스트").build();
        UserSearchCondition dateOnly = UserSearchCondition.builder().fromDate(LocalDateTime.now()).build();
        
        // then
        assertThat(emailOnly.isEmpty()).isFalse();
        assertThat(nameOnly.isEmpty()).isFalse();
        assertThat(dateOnly.isEmpty()).isFalse();
    }
    
    @Test
    @DisplayName("개별 필드 설정 테스트")
    void builder_ShouldSetIndividualFields() {
        // given
        String email = "test@example.com";
        String name = "테스트";
        LocalDateTime fromDate = LocalDateTime.now().minusDays(7);
        LocalDateTime toDate = LocalDateTime.now();
        
        // when
        UserSearchCondition emailCondition = UserSearchCondition.builder().email(email).build();
        UserSearchCondition nameCondition = UserSearchCondition.builder().name(name).build();
        UserSearchCondition fromDateCondition = UserSearchCondition.builder().fromDate(fromDate).build();
        UserSearchCondition toDateCondition = UserSearchCondition.builder().toDate(toDate).build();
        
        // then
        assertThat(emailCondition.getEmail()).isEqualTo(email);
        assertThat(nameCondition.getName()).isEqualTo(name);
        assertThat(fromDateCondition.getFromDate()).isEqualTo(fromDate);
        assertThat(toDateCondition.getToDate()).isEqualTo(toDate);
    }
} 