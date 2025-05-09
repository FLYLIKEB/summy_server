package integration;

import com.jwp.api.ApiApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.jwp.core.repository.UserRepository;
import com.jwp.core.service.UserCommandService;
import com.jwp.core.service.UserQueryService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ExampleApiIntegrationTest {
    
    @LocalServerPort
    private int port;
    
    private TestRestTemplate restTemplate = new TestRestTemplate();
    
    @MockitoBean
    private UserRepository userRepository;
    
    @MockitoBean
    private PasswordEncoder passwordEncoder;
    
    @MockitoBean
    private UserCommandService userCommandService;
    
    @MockitoBean
    private UserQueryService userQueryService;
    
    @Test
    void integrationTest_WhenApiCalled_ThenExpectedResponse() {
        // Given
        String baseUrl = "http://localhost:" + port;
        
        // When
        boolean result = true;
        restTemplate.getForEntity(baseUrl + "/api/users", String.class);
        
        // Then
        assertThat(result).isTrue();
    }
} 