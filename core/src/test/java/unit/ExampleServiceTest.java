package unit;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ExampleServiceTest {
    
    @Test
    void unitTest_WhenCondition_ThenExpectedResult() {
        // Given
        
        // When
        boolean result = true;
        
        // Then
        assertThat(result).isTrue();
    }
} 