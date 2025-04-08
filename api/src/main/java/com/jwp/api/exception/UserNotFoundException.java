package com.jwp.api.exception;

/**
 * 사용자를 찾을 수 없을 때 발생하는 예외
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * 기본 생성자
     * @param userId 찾을 수 없는 사용자 ID
     */
    public UserNotFoundException(Long userId) {
        super("사용자를 찾을 수 없습니다. ID: " + userId);
    }
}