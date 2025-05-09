package integration;

import com.jwp.api.ApiApplication;
import com.jwp.core.config.JpaConfig;
import com.jwp.core.domain.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.jwp.core.repository.UserRepository;
import com.jwp.core.service.UserCommandService;
import com.jwp.core.service.UserQueryService;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EntityScan(basePackages = "com.jwp.core.domain")
@ActiveProfiles("test")
public class ExampleApiIntegrationTest {
    
    @LocalServerPort
    private int port;
    
    private TestRestTemplate restTemplate = new TestRestTemplate();
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @MockBean
    private UserCommandService userCommandService;
    
    @MockBean
    private UserQueryService userQueryService;
    
    @Test
    void integrationTest_WhenApiCalled_ThenExpectedResponse() {
        // Given
        String baseUrl = "http://localhost:" + port;
        
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/api/users", String.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
} 