package com.jwp.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 관련 설정
 * CORS 설정 등 웹 관련 설정을 정의합니다.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * CORS 설정
     * 클라이언트 측에서 발생하는 CORS 문제를 해결하기 위한 설정입니다.
     * 
     * @param registry CORS 레지스트리
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                    "http://localhost:3000",  // 프론트엔드 개발 서버
                    "http://52.78.150.124:8080", // 외부 서버
                    "https://summy-sigma.vercel.app" // Vercel 배포 사이트
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
                
        // XAI API 경로에 대한 CORS 설정 추가
        registry.addMapping("/api/xai/**")
                .allowedOrigins(
                    "http://localhost:3000",
                    "http://52.78.150.124:8080",
                    "https://summy-sigma.vercel.app"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
} 