package com.jwp.core.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 외부 API 호출을 위한 서비스 클래스
 * WebClient를 사용하여 외부 API와 통신합니다.
 */
@Service
public class ExternalApiService {
    
    private final WebClient webClient;
    
    /**
     * WebClient를 주입받는 생성자
     * @param webClient WebClient 인스턴스
     */
    public ExternalApiService(WebClient webClient) {
        this.webClient = webClient;
    }
    
    /**
     * GET 요청을 수행합니다.
     * @param path 요청 경로
     * @return 응답 문자열
     */
    public Mono<String> getData(String path) {
        return webClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(String.class);
    }
    
    /**
     * 커스텀 헤더와 함께 GET 요청을 수행합니다.
     * @param path 요청 경로
     * @param headers 커스텀 헤더
     * @return 응답 문자열
     */
    public Mono<String> getDataWithHeaders(String path, Map<String, String> headers) {
        return webClient.get()
                .uri(path)
                .headers(httpHeaders -> headers.forEach(httpHeaders::add))
                .retrieve()
                .bodyToMono(String.class);
    }
    
    /**
     * POST 요청을 수행합니다.
     * @param path 요청 경로
     * @param requestBody 요청 본문
     * @param responseType 응답 타입
     * @param <T> 응답 타입 파라미터
     * @return 응답 객체
     */
    public <T> Mono<T> postData(String path, Object requestBody, Class<T> responseType) {
        return webClient.post()
                .uri(path)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(responseType);
    }
    
    /**
     * 커스텀 헤더와 함께 POST 요청을 수행합니다.
     * @param path 요청 경로
     * @param requestBody 요청 본문
     * @param responseType 응답 타입
     * @param headers 커스텀 헤더
     * @param <T> 응답 타입 파라미터
     * @return 응답 객체
     */
    public <T> Mono<T> postDataWithHeaders(String path, Object requestBody, Class<T> responseType, Map<String, String> headers) {
        return webClient.post()
                .uri(path)
                .headers(httpHeaders -> headers.forEach(httpHeaders::add))
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(responseType);
    }
} 