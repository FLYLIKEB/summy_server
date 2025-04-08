package com.jwp.core.exception;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * API 에러 응답 객체
 * 클라이언트에게 전달되는 에러 응답의 구조를 정의합니다.
 */
@Getter
public class ErrorResponse {

    private final String code;
    private final String message;
    private final int status;

    // 생성자는 private으로 설정하여 팩토리 메소드를 통해서만 객체 생성 가능
    private ErrorResponse(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    /**
     * ErrorCode로부터 ErrorResponse 객체를 생성합니다.
     *
     * @param code 에러 코드
     * @return ErrorResponse 객체
     */
    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code.name(), code.getMessage(), code.getStatus());
    }

    /**
     * ErrorCode와 커스텀 메시지로부터 ErrorResponse 객체를 생성합니다.
     *
     * @param code 에러 코드
     * @param message 커스텀 에러 메시지
     * @return ErrorResponse 객체
     */
    public static ErrorResponse of(ErrorCode code, String message) {
        return new ErrorResponse(code.name(), message, code.getStatus());
    }
}
