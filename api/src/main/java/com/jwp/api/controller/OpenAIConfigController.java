package com.jwp.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OpenAI API 설정 확인을 위한 컨트롤러
 */
@RestController
@RequestMapping("/api/openai")
public class OpenAIConfigController {
    
    @Value("${external.api.openai.api-key:}")
    private String apiKey;
    
    @Value("${external.api.openai.model:gpt-3.5-turbo}")
    private String model;
    
    @Value("${external.api.base-url:https://api.openai.com/v1}")
    private String baseUrl;
    
    /**
     * OpenAI API 설정 정보 확인
     * @return 설정 정보 (API 키는 마스킹)
     */
    @GetMapping("/config")
    public ResponseEntity<ConfigInfo> getConfig() {
        String maskedApiKey = apiKey != null && apiKey.length() > 10 
            ? apiKey.substring(0, 10) + "***" 
            : "설정되지 않음";
            
        ConfigInfo config = new ConfigInfo(
            baseUrl,
            model,
            maskedApiKey,
            apiKey != null && !apiKey.isEmpty(),
            "OpenAI API 설정 정보"
        );
        
        return ResponseEntity.ok(config);
    }
    
    /**
     * 설정 정보를 담는 클래스
     */
    public static class ConfigInfo {
        private String baseUrl;
        private String model;
        private String maskedApiKey;
        private boolean isApiKeySet;
        private String message;
        
        public ConfigInfo() {}
        
        public ConfigInfo(String baseUrl, String model, String maskedApiKey, boolean isApiKeySet, String message) {
            this.baseUrl = baseUrl;
            this.model = model;
            this.maskedApiKey = maskedApiKey;
            this.isApiKeySet = isApiKeySet;
            this.message = message;
        }
        
        // Getters and Setters
        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        
        public String getMaskedApiKey() { return maskedApiKey; }
        public void setMaskedApiKey(String maskedApiKey) { this.maskedApiKey = maskedApiKey; }
        
        public boolean isApiKeySet() { return isApiKeySet; }
        public void setApiKeySet(boolean apiKeySet) { isApiKeySet = apiKeySet; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
} 