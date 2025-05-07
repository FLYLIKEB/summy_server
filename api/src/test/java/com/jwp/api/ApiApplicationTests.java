package com.jwp.api;

import com.jwp.api.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles("test")
@Tag("integration")
class ApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
