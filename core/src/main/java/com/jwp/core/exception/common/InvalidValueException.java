package com.jwp.core.exception.common;

import com.jwp.core.exception.ErrorCode;

/**
 * 잘못된 값 예외
 * 유효하지 않은 입력값이 있을 때 발생합니다.
 */
public class InvalidValueException extends BusinessException {
    
    public InvalidValueException() {
        super(ErrorCode.INVALID_INPUT_VALUE);
    }
    
    public InvalidValueException(String message) {
        super(ErrorCode.INVALID_INPUT_VALUE, message);
    }
    
    public InvalidValueException(String fieldName, Object value) {
        super(ErrorCode.INVALID_INPUT_VALUE, 
                String.format("잘못된 입력값: %s (값: %s)", fieldName, value));
    }
} 