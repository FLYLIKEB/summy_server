package com.jwp.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jwp.core.domain.User;
import com.jwp.core.domain.UserStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("UserService 테스트")
@ExtendWith(MockitoExtension.class)
@Tag("unit")
class UserServiceTest {
    
    @InjectMocks
    private UserService userService;
    
    @Mock
    private UserCommandService commandService;
    
    @Mock
    private UserQueryService queryService;

    @Nested
    @DisplayName("사용자 생성 시")
    class CreateUser {
        
        @Test
        @DisplayName("새로운 이메일로 가입하면 성공한다")
        void createUser_WhenNewEmail_ThenSuccess() {
            // Given
            User user = createTestUser("test@example.com");
            given(commandService.createUser(any(User.class))).willReturn(user);
            
            // When
            User savedUser = userService.createUser(user);
            
            // Then
            assertThat(savedUser)
                    .isNotNull()
                    .extracting("email", "name")
                    .containsExactly(user.getEmail(), user.getName());
        }
        
        @Test
        @DisplayName("중복된 이메일로 가입하면 예외가 발생한다")
        void createUser_WhenDuplicateEmail_ThenThrowException() {
            // Given
            User user = createTestUser("test@example.com");
            given(commandService.createUser(any(User.class))).willThrow(new IllegalArgumentException("이미 존재하는 이메일입니다."));
            
            // When & Then
            assertThatThrownBy(() -> userService.createUser(user))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 존재하는 이메일입니다.");
        }
    }

    @Nested
    @DisplayName("사용자 조회 시")
    class FindUser {
        
        @Test
        @DisplayName("존재하는 이메일로 조회하면 사용자를 반환한다")
        void findByEmail_WhenUserExists_ThenReturnUser() {
            // Given
            User user = createTestUser("test@example.com");
            given(queryService.findByEmail(user.getEmail())).willReturn(Optional.of(user));
            
            // When
            Optional<User> foundUser = userService.findByEmail(user.getEmail());
            
            // Then
            assertThat(foundUser)
                    .isPresent()
                    .get()
                    .extracting("email", "name")
                    .containsExactly(user.getEmail(), user.getName());
        }
    }

    private User createTestUser(String email) {
        return User.builder()
                .email(email)
                .name("Test User")
                .password("password123")
                .status(UserStatus.ACTIVE)
                .build();
    }
} 