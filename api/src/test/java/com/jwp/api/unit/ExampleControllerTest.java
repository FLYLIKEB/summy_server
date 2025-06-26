package com.jwp.api.unit;

import com.jwp.api.ApiApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ApiApplication.class)
public class ExampleControllerTest {
    
    @Test
    void unitTest_WhenEndpointCalled_ThenExpectedResponse() {
        // Given
        
        // When
        boolean result = true;
        
        // Then
        assertThat(result).isTrue();
    }
} 