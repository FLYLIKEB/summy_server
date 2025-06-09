package com.jwp.api.exception;

/**
 * 파일 업로드 예외
 * 파일 업로드 과정에서 발생하는 예외를 처리합니다.
 */
public class FileUploadException extends RuntimeException {
    
    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
} 