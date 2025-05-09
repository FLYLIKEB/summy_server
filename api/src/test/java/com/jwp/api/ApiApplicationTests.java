package com.jwp.api;

import com.jwp.core.domain.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.jwp.core.repository.UserRepository;
import com.jwp.core.service.UserCommandService;
import com.jwp.core.service.UserQueryService;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EntityScan(basePackages = "com.jwp.core.domain")
@ActiveProfiles("test")
class ApiApplicationTests {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return Mockito.mock(PasswordEncoder.class);
        }
        
        @Bean
        public UserCommandService userCommandService() {
            return Mockito.mock(UserCommandService.class);
        }
        
        @Bean
        public UserQueryService userQueryService() {
            return Mockito.mock(UserQueryService.class);
        }
    }
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserCommandService userCommandService;
    
    @Autowired
    private UserQueryService userQueryService;

    @Test
    void contextLoads() {
        assertThat(userRepository).isNotNull();
        assertThat(passwordEncoder).isNotNull();
        assertThat(userCommandService).isNotNull();
        assertThat(userQueryService).isNotNull();
    }
}
