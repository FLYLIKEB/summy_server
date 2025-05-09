package com.jwp.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.jwp.core.repository.UserRepository;
import com.jwp.core.service.UserCommandService;
import com.jwp.core.service.UserQueryService;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
class ApiApplicationTests {

    @MockBean
    private UserCommandService userCommandService;
    
    @MockBean
    private UserQueryService userQueryService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        assertThat(userRepository).isNotNull();
        assertThat(passwordEncoder).isNotNull();
        assertThat(userCommandService).isNotNull();
        assertThat(userQueryService).isNotNull();
    }
}
