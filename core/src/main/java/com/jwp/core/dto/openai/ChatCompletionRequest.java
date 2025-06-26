package com.jwp.core.dto.openai;

import java.util.List;

/**
 * OpenAI Chat Completion API 요청을 위한 DTO 클래스
 */
public class ChatCompletionRequest {
    private String model;
    private List<Message> messages;
    private double temperature;
    private int max_tokens;

    /**
     * 기본 생성자
     */
    public ChatCompletionRequest() {}

    /**
     * 모든 필드를 포함하는 생성자
     * @param model 사용할 모델
     * @param messages 메시지 목록
     * @param temperature 온도 (창의성 조절)
     * @param max_tokens 최대 토큰 수
     */
    public ChatCompletionRequest(String model, List<Message> messages, double temperature, int max_tokens) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
        this.max_tokens = max_tokens;
    }

    // Getters and Setters
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getMax_tokens() {
        return max_tokens;
    }

    public void setMax_tokens(int max_tokens) {
        this.max_tokens = max_tokens;
    }

    /**
     * Builder 클래스
     */
    public static class Builder {
        private String model;
        private List<Message> messages;
        private double temperature = 0.7;
        private int max_tokens = 1000;

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder messages(List<Message> messages) {
            this.messages = messages;
            return this;
        }

        public Builder temperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder max_tokens(int max_tokens) {
            this.max_tokens = max_tokens;
            return this;
        }

        public ChatCompletionRequest build() {
            return new ChatCompletionRequest(model, messages, temperature, max_tokens);
        }
    }

    /**
     * Builder 인스턴스를 반환하는 정적 메서드
     * @return Builder 인스턴스
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 메시지를 나타내는 내부 클래스
     */
    public static class Message {
        private String role;
        private String content;

        /**
         * 기본 생성자
         */
        public Message() {}

        /**
         * 모든 필드를 포함하는 생성자
         * @param role 메시지 역할 (user, assistant, system)
         * @param content 메시지 내용
         */
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        // Getters and Setters
        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        /**
         * Builder 클래스
         */
        public static class Builder {
            private String role;
            private String content;

            public Builder role(String role) {
                this.role = role;
                return this;
            }

            public Builder content(String content) {
                this.content = content;
                return this;
            }

            public Message build() {
                return new Message(role, content);
            }
        }

        /**
         * Builder 인스턴스를 반환하는 정적 메서드
         * @return Builder 인스턴스
         */
        public static Builder builder() {
            return new Builder();
        }
    }
} 