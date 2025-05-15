package com.jwp.api.controller;

import com.jwp.api.dto.xai.XAIMessageRequestDto;
import com.jwp.api.dto.xai.XAIResponseDto;
import com.jwp.api.service.XAIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/xai")
@RequiredArgsConstructor
@Tag(name = "xAI API", description = "xAI API 호출 관련 엔드포인트")
public class XAIController {

    private final XAIService xaiService;

    @PostMapping("/complete")
    @Operation(summary = "xAI API 호출", description = "사용자 메시지를 받아 xAI API에 요청하고 응답을 반환합니다.")
    public ResponseEntity<XAIResponseDto> generateCompletion(@RequestBody XAIMessageRequestDto requestDto) {
        XAIResponseDto response = xaiService.generateCompletion(requestDto.getMessage());
        return ResponseEntity.ok(response);
    }
} 