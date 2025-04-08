package com.jwp.core.exception.common;

import com.jwp.core.exception.BaseException;
import com.jwp.core.exception.ErrorCode;

/**
 * 비즈니스 예외 클래스
 * 애플리케이션의 비즈니스 로직에서 발생하는 예외의 기본 클래스입니다.
 */
public class BusinessException extends BaseException {

    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
