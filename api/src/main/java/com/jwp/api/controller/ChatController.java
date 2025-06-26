package com.jwp.api.controller;

import com.jwp.core.service.OpenAIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * ChatGPT API 테스트를 위한 컨트롤러
 * OpenAI API와의 연결 상태를 확인하고 테스트할 수 있는 엔드포인트를 제공합니다.
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    private final OpenAIService openAIService;
    
    /**
     * OpenAIService를 주입받는 생성자
     * @param openAIService OpenAI 서비스
     */
    public ChatController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }
    
    /**
     * 간단한 ChatGPT 연동 테스트 엔드포인트
     * @return ChatGPT 응답
     */
    @GetMapping("/test")
    public Mono<ResponseEntity<String>> simpleTest() {
        return openAIService.getChatCompletion("안녕하세요! 연결 테스트입니다. 간단히 응답해주세요.")
                .map(response -> ResponseEntity.ok("✅ ChatGPT 연동 성공!\n응답: " + response))
                .onErrorReturn(ResponseEntity.status(500)
                        .body("❌ ChatGPT 연동 실패. API 키 또는 네트워크를 확인해주세요."));
    }
    
    /**
     * 사용자 정의 메시지로 ChatGPT 테스트
     * @param request 채팅 요청
     * @return ChatGPT 응답
     */
    @PostMapping("/test")
    public Mono<ResponseEntity<ChatResponse>> customTest(@RequestBody ChatRequest request) {
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            return Mono.just(ResponseEntity.badRequest()
                    .body(new ChatResponse("❌ 오류", "메시지가 비어있습니다.")));
        }
        
        return openAIService.getChatCompletion(request.getMessage())
                .map(response -> ResponseEntity.ok(new ChatResponse("✅ 성공", response)))
                .onErrorReturn(ResponseEntity.status(500)
                        .body(new ChatResponse("❌ 오류", "ChatGPT API 호출에 실패했습니다.")));
    }
    
    /**
     * 시스템 프롬프트와 함께 ChatGPT 테스트
     * @param request 시스템 프롬프트가 포함된 채팅 요청
     * @return ChatGPT 응답
     */
    @PostMapping("/test-with-system")
    public Mono<ResponseEntity<ChatResponse>> testWithSystemPrompt(@RequestBody SystemChatRequest request) {
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            return Mono.just(ResponseEntity.badRequest()
                    .body(new ChatResponse("❌ 오류", "메시지가 비어있습니다.")));
        }
        
        String systemPrompt = request.getSystemPrompt() != null ? request.getSystemPrompt() : "당신은 도움이 되는 AI 어시스턴트입니다.";
        
        return openAIService.getChatCompletionWithSystemPrompt(systemPrompt, request.getMessage())
                .map(response -> ResponseEntity.ok(new ChatResponse("✅ 성공", response)))
                .onErrorReturn(ResponseEntity.status(500)
                        .body(new ChatResponse("❌ 오류", "ChatGPT API 호출에 실패했습니다.")));
    }
    
    /**
     * API 상태 확인 엔드포인트
     * @return API 상태 정보
     */
    @GetMapping("/status")
    public ResponseEntity<ApiStatus> getStatus() {
        return ResponseEntity.ok(new ApiStatus(
                "ChatGPT API 상태 확인",
                "이 엔드포인트는 API가 실행 중임을 확인합니다.",
                System.currentTimeMillis()
        ));
    }
    
    /**
     * 채팅 요청을 위한 DTO 클래스
     */
    public static class ChatRequest {
        private String message;
        
        public ChatRequest() {}
        
        public ChatRequest(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
    
    /**
     * 시스템 프롬프트가 포함된 채팅 요청을 위한 DTO 클래스
     */
    public static class SystemChatRequest {
        private String systemPrompt;
        private String message;
        
        public SystemChatRequest() {}
        
        public SystemChatRequest(String systemPrompt, String message) {
            this.systemPrompt = systemPrompt;
            this.message = message;
        }
        
        public String getSystemPrompt() {
            return systemPrompt;
        }
        
        public void setSystemPrompt(String systemPrompt) {
            this.systemPrompt = systemPrompt;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
    
    /**
     * 채팅 응답을 위한 DTO 클래스
     */
    public static class ChatResponse {
        private String status;
        private String response;
        
        public ChatResponse() {}
        
        public ChatResponse(String status, String response) {
            this.status = status;
            this.response = response;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public String getResponse() {
            return response;
        }
        
        public void setResponse(String response) {
            this.response = response;
        }
    }
    
    /**
     * API 상태 정보를 위한 DTO 클래스
     */
    public static class ApiStatus {
        private String service;
        private String description;
        private long timestamp;
        
        public ApiStatus() {}
        
        public ApiStatus(String service, String description, long timestamp) {
            this.service = service;
            this.description = description;
            this.timestamp = timestamp;
        }
        
        public String getService() {
            return service;
        }
        
        public void setService(String service) {
            this.service = service;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
        
        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
} 