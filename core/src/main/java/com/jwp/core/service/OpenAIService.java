package com.jwp.core.service;

import com.jwp.core.dto.openai.ChatCompletionRequest;
import com.jwp.core.dto.openai.ChatCompletionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * OpenAI API와 통신하는 서비스 클래스
 * ChatGPT API를 호출하여 대화형 AI 응답을 제공합니다.
 */
@Service
public class OpenAIService {
    
    private final ExternalApiService externalApiService;
    
    @Value("${external.api.openai.model:gpt-3.5-turbo}")
    private String model;
    
    @Value("${external.api.openai.api-key:}")
    private String apiKey;
    
    /**
     * ExternalApiService를 주입받는 생성자
     * @param externalApiService 외부 API 호출 서비스
     */
    public OpenAIService(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }
    
    /**
     * ChatGPT Chat Completion 요청을 생성합니다.
     * @param userMessage 사용자 메시지
     * @return ChatCompletionResponse Mono
     */
    public Mono<ChatCompletionResponse> createChatCompletion(String userMessage) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return Mono.error(new RuntimeException("OpenAI API 키가 설정되지 않았습니다."));
        }
        
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(List.of(
                        ChatCompletionRequest.Message.builder()
                                .role("user")
                                .content(userMessage)
                                .build()
                ))
                .temperature(0.7)
                .max_tokens(1000)
                .build();
        
        return externalApiService.postDataWithHeaders(
                "/chat/completions",
                request,
                ChatCompletionResponse.class,
                Map.of("Authorization", "Bearer " + apiKey)
        );
    }
    
    /**
     * ChatGPT 응답에서 텍스트만 추출하여 반환합니다.
     * @param userMessage 사용자 메시지
     * @return 응답 텍스트 Mono
     */
    public Mono<String> getChatCompletion(String userMessage) {
        return createChatCompletion(userMessage)
                .map(response -> {
                    if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                        return response.getChoices().get(0).getMessage().getContent();
                    }
                    return "응답을 생성할 수 없습니다.";
                })
                .onErrorReturn("ChatGPT API 호출에 실패했습니다.");
    }
    
    /**
     * 시스템 프롬프트와 함께 ChatGPT 요청을 생성합니다.
     * @param systemPrompt 시스템 프롬프트
     * @param userMessage 사용자 메시지
     * @return ChatCompletionResponse Mono
     */
    public Mono<ChatCompletionResponse> createChatCompletionWithSystemPrompt(String systemPrompt, String userMessage) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return Mono.error(new RuntimeException("OpenAI API 키가 설정되지 않았습니다."));
        }
        
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(List.of(
                        ChatCompletionRequest.Message.builder()
                                .role("system")
                                .content(systemPrompt)
                                .build(),
                        ChatCompletionRequest.Message.builder()
                                .role("user")
                                .content(userMessage)
                                .build()
                ))
                .temperature(0.7)
                .max_tokens(1000)
                .build();
        
        return externalApiService.postDataWithHeaders(
                "/chat/completions",
                request,
                ChatCompletionResponse.class,
                Map.of("Authorization", "Bearer " + apiKey)
        );
    }
    
    /**
     * 시스템 프롬프트와 함께 ChatGPT 응답에서 텍스트만 추출하여 반환합니다.
     * @param systemPrompt 시스템 프롬프트
     * @param userMessage 사용자 메시지
     * @return 응답 텍스트 Mono
     */
    public Mono<String> getChatCompletionWithSystemPrompt(String systemPrompt, String userMessage) {
        return createChatCompletionWithSystemPrompt(systemPrompt, userMessage)
                .map(response -> {
                    if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                        return response.getChoices().get(0).getMessage().getContent();
                    }
                    return "응답을 생성할 수 없습니다.";
                })
                .onErrorReturn("ChatGPT API 호출에 실패했습니다.");
    }
} 