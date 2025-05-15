package com.jwp.api.service;

import com.jwp.api.dto.xai.XAIRequestDto;
import com.jwp.api.dto.xai.XAIResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class XAIService {

    private final Logger log = LoggerFactory.getLogger(XAIService.class);
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
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .defaultHeader("Content-Type", "application/json")
            .build();
        log.info("XAIService initialized with baseUrl: {}", baseUrl);
    }

    public XAIResponseDto generateCompletion(String userMessage) {
        log.info("Generating completion for message: {}", userMessage);
        
        if (apiKey == null || apiKey.isEmpty() || "${XAI_API_KEY:}".equals(apiKey)) {
            return createErrorResponse("xAI API 키가 설정되지 않았습니다. 환경 변수 XAI_API_KEY를 확인하세요.");
        }
        
        try {
            XAIRequestDto.Message message = new XAIRequestDto.Message("user", userMessage);
            XAIRequestDto requestDto = XAIRequestDto.builder()
                .messages(Collections.singletonList(message))
                .model("grok-3-beta")
                .temperature(0.7)
                .stream(false)
                .build();
            
            log.debug("Sending request to xAI API: {}", requestDto);
            
            XAIResponseDto response = webClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(XAIResponseDto.class)
                .block();
            
            log.info("Received response from xAI API: id={}", response.getId());
            return response;
        } catch (WebClientResponseException e) {
            log.error("Error while calling xAI API: {} - Response body: {}", e.getMessage(), e.getResponseBodyAsString(), e);
            return createErrorResponse("API 호출 중 오류가 발생했습니다: " + e.getStatusCode() + " " + e.getStatusText() + " from " + e.getRequest().getMethod() + " " + e.getRequest().getURI());
        } catch (Exception e) {
            log.error("Error while calling xAI API: {}", e.getMessage(), e);
            return createErrorResponse("API 호출 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    private XAIResponseDto createErrorResponse(String errorMessage) {
        XAIResponseDto.Message message = new XAIResponseDto.Message("assistant", errorMessage);
        XAIResponseDto.Choice choice = new XAIResponseDto.Choice(message, 0, "error");
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