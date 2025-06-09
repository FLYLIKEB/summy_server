package com.jwp.api.service;

import com.jwp.api.dto.response.FileUploadResponse;
import com.jwp.api.exception.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 파일 업로드 서비스
 * 파일 업로드 관련 비즈니스 로직을 처리합니다.
 */
@Slf4j
@Service
public class FileUploadService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${file.max-size:10485760}") // 10MB
    private long maxFileSize;

    /**
     * 파일 업로드 처리
     * @param file 업로드할 파일
     * @return 업로드 결과
     */
    public FileUploadResponse uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileUploadException("업로드할 파일이 없습니다.");
        }

        if (file.getSize() > maxFileSize) {
            throw new FileUploadException("파일 크기가 제한을 초과했습니다.");
        }

        try {
            // 업로드 디렉토리 생성
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일명 생성 (UUID + 원본 파일명)
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? 
                originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String newFilename = UUID.randomUUID().toString() + extension;

            // 파일 저장
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);

            // 파일 URL 생성
            String fileUrl = "/uploads/" + newFilename;

            return FileUploadResponse.builder()
                    .success(true)
                    .message("파일이 성공적으로 업로드되었습니다.")
                    .fileUrl(fileUrl)
                    .fileName(originalFilename)
                    .fileSize(file.getSize())
                    .build();

        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생", e);
            throw new FileUploadException("파일 업로드 중 오류가 발생했습니다.");
        }
    }
} 