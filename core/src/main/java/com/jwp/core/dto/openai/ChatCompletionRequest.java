package com.jwp.core.dto.openai;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatCompletionRequest {
    private String model;
    private List<Message> messages;
    private double temperature;
    private int max_tokens;

    @Getter
    @Builder
    public static class Message {
        private String role;
        private String content;
    }
} 