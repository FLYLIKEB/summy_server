package com.jwp.core.repository;

import com.jwp.core.exception.common.InvalidValueException;
import lombok.Getter;
import java.time.LocalDateTime;

/**
 * 사용자 검색 조건
 * 다양한 조건으로 사용자를 검색할 때 사용되는 조건 객체입니다.
 */
@Getter
public class UserSearchCondition {
    /** 검색할 이메일 */
    private String email;
    
    /** 검색할 이름 */
    private String name;
    
    /** 검색 시작 일시 */
    private LocalDateTime fromDate;
    
    /** 검색 종료 일시 */
    private LocalDateTime toDate;
    
    /**
     * 기본 생성자 (JPA 요구사항)
     */
    protected UserSearchCondition() {
    }
    
    /**
     * 검색 조건의 생성자
     * 직접 생성 대신 빌더를 사용하는 것을 권장합니다.
     */
    private UserSearchCondition(String email, String name, LocalDateTime fromDate, LocalDateTime toDate) {
        this.email = email;
        this.name = name;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
    
    /**
     * 빌더 생성
     * @return 새로운 빌더 인스턴스
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * 모든 필드가 null인지 확인
     * 검색 조건이 모두 비어 있는지 확인합니다.
     * @return 모든 필드가 null인 경우 true
     */
    public boolean isEmpty() {
        return email == null && name == null && fromDate == null && toDate == null;
    }
    
    /**
     * 사용자 검색 조건 빌더
     */
    public static class Builder {
        private String email;
        private String name;
        private LocalDateTime fromDate;
        private LocalDateTime toDate;
        
        /**
         * 이메일 설정
         * @param email 검색할 이메일
         * @return 빌더 인스턴스
         */
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        /**
         * 이름 설정
         * @param name 검색할 이름
         * @return 빌더 인스턴스
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * 검색 시작 일시 설정
         * @param fromDate 검색 시작 일시
         * @return 빌더 인스턴스
         */
        public Builder fromDate(LocalDateTime fromDate) {
            this.fromDate = fromDate;
            return this;
        }
        
        /**
         * 검색 종료 일시 설정
         * @param toDate 검색 종료 일시
         * @return 빌더 인스턴스
         */
        public Builder toDate(LocalDateTime toDate) {
            this.toDate = toDate;
            return this;
        }
        
        /**
         * 날짜 범위 설정
         * @param fromDate 검색 시작 일시
         * @param toDate 검색 종료 일시
         * @return 빌더 인스턴스
         * @throws InvalidValueException fromDate가 toDate보다 나중인 경우
         */
        public Builder dateRange(LocalDateTime fromDate, LocalDateTime toDate) {
            if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
                throw new InvalidValueException("dateRange", 
                    String.format("시작 일시(%s)는 종료 일시(%s)보다 이전이어야 합니다", fromDate, toDate));
            }
            this.fromDate = fromDate;
            this.toDate = toDate;
            return this;
        }
        
        /**
         * 검색 조건 객체 생성
         * @return 설정된 값으로 생성된 UserSearchCondition
         */
        public UserSearchCondition build() {
            return new UserSearchCondition(email, name, fromDate, toDate);
        }
    }
}