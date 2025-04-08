package com.jwp.core.domain;

/**
 * 사용자 상태 열거형
 * 사용자 계정의 상태를 나타냅니다.
 */
public enum UserStatus {
    /**
     * 활성 상태
     * 정상적으로 서비스를 이용할 수 있는 상태
     */
    ACTIVE,
    
    /**
     * 휴면 상태
     * 장기간 미사용으로 인해 제한된 상태
     */
    INACTIVE,
    
    /**
     * 정지 상태
     * 관리자에 의해 계정 사용이 중지된 상태
     */
    SUSPENDED,
    
    /**
     * 탈퇴 상태
     * 사용자가 회원 탈퇴한 상태
     */
    WITHDRAWN
} 