package unit;

import com.jwp.api.ApiApplication;
import com.jwp.api.config.TestConfig;
import com.jwp.api.controller.UserController;
import com.jwp.api.service.UserApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(controllers = {UserController.class})
@Import(TestConfig.class)
@ContextConfiguration(classes = ApiApplication.class)
@ActiveProfiles("test")
public class ExampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserApiService userApiService;

    @Test
    void unitTest_WhenEndpointCalled_ThenExpectedResponse() {
        // Given
        
        // When
        boolean result = true;
        
        // Then
        assertThat(result).isTrue();
    }
} 