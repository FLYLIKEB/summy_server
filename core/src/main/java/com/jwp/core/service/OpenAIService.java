package com.jwp.core.service;

import com.jwp.core.dto.openai.ChatCompletionRequest;
import com.jwp.core.dto.openai.ChatCompletionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAIService {
    
    private final ExternalApiService externalApiService;
    
    @Value("${external.api.openai.model}")
    private String model;
    
    @Value("${external.api.openai.api-key}")
    private String apiKey;
    
    public Mono<ChatCompletionResponse> createChatCompletion(String userMessage) {
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
    
    public Mono<String> getChatCompletion(String userMessage) {
        return createChatCompletion(userMessage)
                .map(response -> response.getChoices().get(0).getMessage().getContent());
    }
} 