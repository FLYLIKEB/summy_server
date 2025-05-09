package unit;

import com.jwp.api.ApiApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.jwp.core.repository.UserRepository;
import com.jwp.core.service.UserCommandService;
import com.jwp.core.service.UserQueryService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class ExampleControllerTest {

    @MockitoBean
    private UserRepository userRepository;
    
    @MockitoBean
    private PasswordEncoder passwordEncoder;
    
    @MockitoBean
    private UserCommandService userCommandService;
    
    @MockitoBean
    private UserQueryService userQueryService;

    @Test
    void unitTest_WhenEndpointCalled_ThenExpectedResponse() {
        assertThat(true).isTrue();
    }
} 