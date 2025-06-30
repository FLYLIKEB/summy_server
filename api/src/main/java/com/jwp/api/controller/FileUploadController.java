package com.jwp.api.controller;

import com.jwp.api.dto.response.FileUploadResponse;
import com.jwp.api.exception.FileUploadException;
import com.jwp.api.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

/**
 * 파일 업로드 API 컨트롤러
 * 파일 업로드 관련 API 엔드포인트를 제공합니다.
 */
@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@Tag(name = "파일 업로드", description = "파일 업로드 API")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    /**
     * 파일 업로드 API
     * @param file 업로드할 파일
     * @return 업로드 결과
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "파일 업로드", description = "파일을 서버에 업로드합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "파일 업로드 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "413", description = "파일 크기 초과", content = @Content)
    })
    public ResponseEntity<FileUploadResponse> uploadFile(
            @Parameter(description = "업로드할 파일", required = true)
            @RequestParam("file") MultipartFile file) {
        
        // FileUploadResponse response = fileUploadService.uploadFile(file);
        // return ResponseEntity.ok(response);
        try {
            FileUploadResponse response = fileUploadService.uploadFile(file);
            return ResponseEntity.ok(response);
        } catch (FileUploadException e) {
            FileUploadResponse errorResponse = FileUploadResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
} 