package com.jwp.core.controller;

import com.jwp.core.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class OpenAITestController {
    
    private final OpenAIService openAIService;
    
    @GetMapping("/openai")
    public Mono<String> testOpenAI() {
        return openAIService.getChatCompletion("Hello, are you working?");
    }
} 