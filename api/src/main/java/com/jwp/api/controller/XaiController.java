package com.jwp.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

/**
 * XAI API 컨트롤러
 * XAI 관련 API 엔드포인트를 제공합니다.
 */
@RestController
@RequestMapping("/api/xai")
@Tag(name = "XAI", description = "XAI 관련 API")
@CrossOrigin(origins = {"http://localhost:3000", "http://52.78.150.124:8080", "https://summy-sigma.vercel.app"}, allowCredentials = "true")
public class XaiController {

    /**
     * 텍스트 요약 API
     * 클라이언트가 보낸 텍스트를 요약합니다.
     *
     * @param request 요약 요청 본문
     * @return 요약 결과
     */
    @PostMapping("/complete")
    @Operation(summary = "텍스트 요약", description = "입력된 텍스트를 요약합니다.")
    public ResponseEntity<Map<String, String>> summarizeText(@RequestBody Map<String, String> request) {
        // 실제 요약 로직은 여기에 구현
        // 임시 응답 데이터
        Map<String, String> response = new HashMap<>();
        response.put("summary", "요약된 텍스트입니다. 실제 구현 시 이 부분이 대체됩니다.");
        
        return ResponseEntity.ok(response);
    }
} 