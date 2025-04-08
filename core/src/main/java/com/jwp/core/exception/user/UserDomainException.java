package com.jwp.core.exception.user;

import com.jwp.core.exception.BusinessException;
import com.jwp.core.exception.ErrorCode;

/**
 * 사용자 도메인 예외
 * 사용자와 관련된 에러를 다룹니다.
 */
public class UserDomainException extends BusinessException {
    
    public UserDomainException(ErrorCode errorCode) {
        super(errorCode);
    }
    
    public UserDomainException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    /**
     * 이메일 중복 예외 팩토리 메소드
     */
    public static BusinessException emailDuplication(String email) {
        return new UserDomainException(
                ErrorCode.EMAIL_DUPLICATION,
                String.format("이미 존재하는 이메일입니다: %s", email)
        );
    }
    
    /**
     * 사용자 없음 예외 팩토리 메소드
     */
    public static BusinessException userNotFound(String identifier) {
        return new UserDomainException(
                ErrorCode.USER_NOT_FOUND,
                String.format("사용자를 찾을 수 없습니다: %s", identifier)
        );
    }
    
    /**
     * 비활성 사용자 예외 팩토리 메소드
     */
    public static BusinessException inactiveUser(String email) {
        return new UserDomainException(
                ErrorCode.INACTIVE_USER,
                String.format("비활성화된 사용자입니다: %s", email)
        );
    }
} 