package com.jwp.core.exception;

import lombok.Getter;

/**
 * 비즈니스 예외 클래스
 * 상속 대신 조합을 통해 예외 정보를 처리합니다.
 */
@Getter
public class BusinessException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
    
    /**
     * 예외로부터 에러 응답 생성
     */
    public ErrorResponse toErrorResponse() {
        return ErrorResponse.of(errorCode, getMessage());
    }
} 