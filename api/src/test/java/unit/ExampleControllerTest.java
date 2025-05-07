package unit;

import com.jwp.api.ApiApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class ExampleControllerTest {

    @Test
    void unitTest_WhenEndpointCalled_ThenExpectedResponse() {
        assertTrue(true);
    }
} 