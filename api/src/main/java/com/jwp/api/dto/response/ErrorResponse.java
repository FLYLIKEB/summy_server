package com.jwp.api.dto.response;

import java.time.LocalDateTime;

/**
 * API 오류 응답 DTO
 */
public class ErrorResponse {
    private final String code;
    private final String message;
    private final LocalDateTime timestamp;

    /**
     * 생성자
     * @param code 오류 코드
     * @param message 오류 메시지
     */
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
} 