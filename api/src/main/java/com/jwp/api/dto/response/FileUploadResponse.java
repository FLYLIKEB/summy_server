package com.jwp.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 파일 업로드 응답 DTO
 * 파일 업로드 API 응답 데이터를 담는 객체입니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {
    private boolean success;
    private String message;
    private String fileUrl;
    private String fileName;
    private long fileSize;
} 