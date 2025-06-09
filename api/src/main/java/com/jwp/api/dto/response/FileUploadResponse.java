package com.jwp.api.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 파일 업로드 응답 DTO
 * 파일 업로드 API 응답 데이터를 담는 객체입니다.
 */
@Getter
@Builder
public class FileUploadResponse {
    private final boolean success;
    private final String message;
    private final String fileUrl;
    private final String fileName;
    private final long fileSize;
} 