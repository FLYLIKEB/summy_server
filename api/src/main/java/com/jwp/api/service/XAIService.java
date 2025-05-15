package com.jwp.api.service;

import com.jwp.api.dto.xai.XAIRequestDto;
import com.jwp.api.dto.xai.XAIResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class XAIService {

    private final String apiKey;
    private final String baseUrl;
    private final WebClient webClient;

    public XAIService(
        @Value("${xai.api.key}") String apiKey,
        @Value("${xai.api.url}") String baseUrl
    ) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("x-api-key", apiKey)
            .defaultHeader("Content-Type", "application/json")
            .build();
        log.info("XAIService initialized with baseUrl: {}", baseUrl);
    }

    public XAIResponseDto generateCompletion(String userMessage) {
        log.info("Generating completion for message: {}", userMessage);
        
        try {
            // API 요청 객체 생성
            XAIRequestDto.Message message = new XAIRequestDto.Message("user", userMessage);
            XAIRequestDto requestDto = XAIRequestDto.builder()
                .messages(Collections.singletonList(message))
                .model("grok-3-beta")
                .temperature(0.7)
                .stream(false)
                .build();
            
            // API 호출 및 응답 파싱
            XAIResponseDto response = webClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(XAIResponseDto.class)
                .block();
            
            log.info("Received response from xAI API: id={}", response.getId());
            return response;
        } catch (Exception e) {
            log.error("Error while calling xAI API: {}", e.getMessage(), e);
            
            // API 호출 실패 시 대체 응답 생성
            XAIResponseDto.Message errorMessage = new XAIResponseDto.Message(
                "assistant", 
                "API 호출 중 오류가 발생했습니다: " + e.getMessage()
            );
            XAIResponseDto.Choice choice = new XAIResponseDto.Choice(errorMessage, 0, "error");
            XAIResponseDto.Usage usage = new XAIResponseDto.Usage(0, 0, 0);
            
            List<XAIResponseDto.Choice> choices = new ArrayList<>();
            choices.add(choice);
            
            return new XAIResponseDto(
                "error-" + System.currentTimeMillis(), 
                "chat.completion", 
                System.currentTimeMillis() / 1000,
                "grok-3-beta",
                choices,
                usage
            );
        }
    }
} 