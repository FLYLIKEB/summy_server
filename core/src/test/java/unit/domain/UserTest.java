package unit.domain;

import domain.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    
    @Test
    void createUser_WhenValidInput_ThenSuccess() {
        // Given
        String email = "test@example.com";
        String name = "Test User";
        String password = "password123";
        
        // When
        User user = User.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();
        
        // Then
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getPassword()).isEqualTo(password);
    }
} 