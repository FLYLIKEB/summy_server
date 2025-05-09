package com.jwp.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.jwp.core.repository.UserRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = ApiApplication.class)
class ApiApplicationTests {

    @MockitoBean
    private UserRepository userRepository;
    
    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        // 기본 컨텍스트 로드 테스트
    }
}
