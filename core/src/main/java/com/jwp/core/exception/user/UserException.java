package com.jwp.core.exception.user;

import com.jwp.core.exception.BaseException;
import com.jwp.core.exception.ErrorCode;

/**
 * 사용자 관련 예외 클래스
 * 사용자 도메인에서 발생하는 모든 예외의 기본 클래스입니다.
 */
public class UserException extends BaseException {
    
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
    
    public UserException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    public UserException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
} 