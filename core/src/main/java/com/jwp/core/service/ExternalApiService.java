package com.jwp.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExternalApiService {
    
    private final WebClient webClient;
    
    // GET 요청 예시 (기본 헤더 사용)
    public Mono<String> getData(String path) {
        return webClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(String.class);
    }
    
    // GET 요청 예시 (커스텀 헤더 사용)
    public Mono<String> getDataWithHeaders(String path, Map<String, String> headers) {
        return webClient.get()
                .uri(path)
                .headers(httpHeaders -> headers.forEach(httpHeaders::add))
                .retrieve()
                .bodyToMono(String.class);
    }
    
    // POST 요청 예시 (기본 헤더 사용)
    public <T> Mono<T> postData(String path, Object requestBody, Class<T> responseType) {
        return webClient.post()
                .uri(path)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(responseType);
    }
    
    // POST 요청 예시 (커스텀 헤더 사용)
    public <T> Mono<T> postDataWithHeaders(String path, Object requestBody, Class<T> responseType, Map<String, String> headers) {
        return webClient.post()
                .uri(path)
                .headers(httpHeaders -> headers.forEach(httpHeaders::add))
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(responseType);
    }
} 