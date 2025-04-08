package com.jwp.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 설정
 * 보안 관련 설정을 정의합니다.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 보안 필터 체인 설정
     * API 서버이므로 CSRF 보호 및 폼 로그인을 비활성화합니다.
     *
     * @param http HttpSecurity 설정 객체
     * @return 구성된 SecurityFilterChain
     * @throws Exception 보안 설정 중 오류 발생 시
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // API 서버를 위한 CSRF 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // API 서버를 위한 폼 로그인 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // API 서버를 위한 기본 인증 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll() // API 요청은 인증 없이 허용
                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                )
                .build();
    }

    /**
     * 비밀번호 인코더 빈
     * 비밀번호를 안전하게 저장하기 위한 해시 함수를 제공합니다.
     *
     * @return BCrypt 알고리즘을 사용하는 비밀번호 인코더
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}