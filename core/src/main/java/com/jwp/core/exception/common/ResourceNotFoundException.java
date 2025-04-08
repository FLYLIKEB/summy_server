package com.jwp.core.exception.common;

import com.jwp.core.exception.ErrorCode;

/**
 * 리소스 찾을 수 없음 예외
 * 요청한 리소스를 찾을 수 없을 때 발생합니다.
 */
public class ResourceNotFoundException extends BusinessException {
    
    public ResourceNotFoundException() {
        super(ErrorCode.RESOURCE_NOT_FOUND);
    }
    
    public ResourceNotFoundException(String resource) {
        super(ErrorCode.RESOURCE_NOT_FOUND, String.format("요청한 리소스를 찾을 수 없습니다: %s", resource));
    }
    
    public ResourceNotFoundException(String resource, Object identifier) {
        super(ErrorCode.RESOURCE_NOT_FOUND, 
                String.format("ID: %s에 해당하는 %s 리소스를 찾을 수 없습니다.", identifier, resource));
    }
} 