package com.jwp.core.exception.user;

import com.jwp.core.exception.ErrorCode;

/**
 * 사용자 찾을 수 없음 예외
 * 존재하지 않는 사용자를 조회할 때 발생합니다.
 */
public class UserNotFoundException extends UserException {
    
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
    
    public UserNotFoundException(String identifier) {
        super(ErrorCode.USER_NOT_FOUND, String.format("사용자를 찾을 수 없습니다: %s", identifier));
    }
    
    public UserNotFoundException(Long id) {
        super(ErrorCode.USER_NOT_FOUND, String.format("ID에 해당하는 사용자를 찾을 수 없습니다: %d", id));
    }
} 