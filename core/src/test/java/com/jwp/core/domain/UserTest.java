package com.jwp.core.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * User 엔티티 단위 테스트
 */
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
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
    
    @Test
    void updateUserInfo_ShouldChangeValues() {
        // Given
        User user = User.builder()
                .email("test@example.com")
                .name("Original Name")
                .password("originalpass")
                .build();
        
        // When
        user.update("New Name");
        user.changePassword("newpass123");
        user.changeStatus(UserStatus.INACTIVE);
        
        // Then
        assertThat(user.getName()).isEqualTo("New Name");
        assertThat(user.getPassword()).isEqualTo("newpass123");
        assertThat(user.getStatus()).isEqualTo(UserStatus.INACTIVE);
    }
} 