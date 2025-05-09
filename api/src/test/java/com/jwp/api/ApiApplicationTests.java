package com.jwp.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
class ApiApplicationTests {

    @Test
    void contextLoads() {
        // 컨텍스트 로드 테스트
    }
}
