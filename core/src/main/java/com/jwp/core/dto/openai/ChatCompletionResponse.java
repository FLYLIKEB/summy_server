package com.jwp.core.dto.openai;

import lombok.Getter;

import java.util.List;

@Getter
public class ChatCompletionResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;

    @Getter
    public static class Choice {
        private int index;
        private Message message;
        private String finish_reason;
    }

    @Getter
    public static class Message {
        private String role;
        private String content;
    }

    @Getter
    public static class Usage {
        private int prompt_tokens;
        private int completion_tokens;
        private int total_tokens;
    }
} 