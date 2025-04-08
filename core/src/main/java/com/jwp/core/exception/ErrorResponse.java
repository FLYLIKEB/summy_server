package com.jwp.core.exception;

import lombok.Builder;
import lombok.Getter;

/**
 * 에러 응답 객체
 * 예외 발생 시 클라이언트에게 반환되는 응답 형식을 정의합니다.
 */
@Getter
@Builder
public class ErrorResponse {
    private final String code;
    private final String message;
    private final int status;

    /**
     * ErrorCode로부터 ErrorResponse 생성
     */
    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus())
                .build();
    }

    /**
     * ErrorCode와 메시지로부터 ErrorResponse 생성
     */
    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(message)
                .status(errorCode.getStatus())
                .build();
    }
}
