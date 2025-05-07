package com.jwp.api.config;

import com.jwp.api.service.UserApiService;
import com.jwp.core.repository.UserRepository;
import com.jwp.core.service.UserCommandService;
import com.jwp.core.service.UserQueryService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 테스트를 위한 빈 설정
 * 테스트 환경에서 필요한 Mock 빈들을 제공합니다.
 */
@TestConfiguration
public class TestConfig {

    /**
     * 테스트용 UserCommandService Mock 빈
     * @return UserCommandService Mock 객체
     */
    @Bean
    @Primary
    public UserCommandService userCommandService() {
        return Mockito.mock(UserCommandService.class);
    }

    /**
     * 테스트용 UserQueryService Mock 빈
     * @return UserQueryService Mock 객체
     */
    @Bean
    @Primary
    public UserQueryService userQueryService() {
        return Mockito.mock(UserQueryService.class);
    }

    /**
     * 테스트용 UserRepository Mock 빈
     * @return UserRepository Mock 객체
     */
    @Bean
    @Primary
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    /**
     * 테스트용 UserApiService Mock 빈
     * @return UserApiService Mock 객체
     */
    @Bean
    @Primary
    public UserApiService userApiService() {
        return Mockito.mock(UserApiService.class);
    }

    /**
     * 테스트용 PasswordEncoder 빈
     * @return PasswordEncoder 객체
     */
    @Bean
    @Primary
    public PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 