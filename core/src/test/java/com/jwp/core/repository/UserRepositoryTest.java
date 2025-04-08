package com.jwp.core.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.jwp.core.CoreTestApplication;
import com.jwp.core.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = CoreTestApplication.class)
@ActiveProfiles("test")
class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void existsByEmail_WhenEmailExists_ThenReturnTrue() {
        // Given
        String email = "test@example.com";
        User user = User.builder()
                .email(email)
                .name("Test User")
                .password("password123")
                .build();
        userRepository.save(user);
        
        // When
        boolean exists = userRepository.existsByEmail(email);
        
        // Then
        assertThat(exists).isTrue();
    }
    
    @Test
    void existsByEmail_WhenEmailDoesNotExist_ThenReturnFalse() {
        // Given
        String email = "nonexistent@example.com";
        
        // When
        boolean exists = userRepository.existsByEmail(email);
        
        // Then
        assertThat(exists).isFalse();
    }
    
    @Test
    void findByEmail_WhenEmailExists_ThenReturnUser() {
        // Given
        String email = "test@example.com";
        User user = User.builder()
                .email(email)
                .name("Test User")
                .password("password123")
                .build();
        userRepository.save(user);
        
        // When
        User foundUser = userRepository.findByEmail(email);
        
        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(email);
        assertThat(foundUser.getName()).isEqualTo("Test User");
    }
} 