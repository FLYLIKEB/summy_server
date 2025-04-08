package com.jwp.core.exception.user;

import com.jwp.core.exception.ErrorCode;

/**
 * 이메일 중복 예외
 * 이미 존재하는 이메일로 회원가입 시도 시 발생합니다.
 */
public class EmailDuplicationException extends UserException {
    
    public EmailDuplicationException() {
        super(ErrorCode.EMAIL_DUPLICATION);
    }
    
    public EmailDuplicationException(String email) {
        super(ErrorCode.EMAIL_DUPLICATION, String.format("이미 존재하는 이메일입니다: %s", email));
    }
} 