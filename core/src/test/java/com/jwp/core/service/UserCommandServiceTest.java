package com.jwp.core.service;

import com.jwp.core.domain.User;
import com.jwp.core.domain.UserStatus;
import com.jwp.core.exception.BusinessException;
import com.jwp.core.exception.user.UserDomainException;
import com.jwp.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserQueryService userQueryService;

    @InjectMocks
    private UserCommandService userCommandService;

    private User testUser;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성
        testUser = User.builder()
                .email("test@example.com")
                .name("테스트 사용자")
                .password("password")
                .status(UserStatus.ACTIVE)
                .build();
        
        // ID 설정
        ReflectionTestUtils.setField(testUser, "id", 1L);
    }

    @Test
    @DisplayName("사용자 생성 성공")
    void createUser_Success() {
        // given
        when(userQueryService.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // when
        User savedUser = userCommandService.createUser(testUser);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(savedUser.getName()).isEqualTo(testUser.getName());
        
        verify(userQueryService, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 생성 실패 - 이메일 중복")
    void createUser_EmailDuplication_ThrowsException() {
        // given
        when(userQueryService.existsByEmail(anyString())).thenReturn(true);

        // when & then
        assertThrows(UserDomainException.class, () -> userCommandService.createUser(testUser));
        
        verify(userQueryService, times(1)).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 생성 실패 - null 입력")
    void createUser_NullInput_ThrowsException() {
        // when & then
        assertThrows(BusinessException.class, () -> userCommandService.createUser(null));
        
        verify(userQueryService, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 정보 업데이트 성공")
    void updateUserInfo_Success() {
        // given
        String newName = "새로운 이름";
        when(userQueryService.findById(anyLong())).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // when
        User updatedUser = userCommandService.updateUserInfo(1L, newName);

        // then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo(newName);
        
        verify(userQueryService, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 정보 업데이트(이메일 기반) 성공")
    void updateUserInfoByEmail_Success() {
        // given
        String newName = "새로운 이름";
        when(userQueryService.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // when
        User updatedUser = userCommandService.updateUserInfo("test@example.com", newName);

        // then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo(newName);
        
        verify(userQueryService, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void changePassword_Success() {
        // given
        String newPassword = "newPassword";
        when(userQueryService.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // when
        User updatedUser = userCommandService.changePassword("test@example.com", newPassword);

        // then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getPassword()).isEqualTo(newPassword);
        
        verify(userQueryService, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 삭제 성공")
    void deleteUser_Success() {
        // given
        when(userQueryService.findById(anyLong())).thenReturn(testUser);
        doNothing().when(userRepository).delete(any(User.class));

        // when
        userCommandService.deleteUser(1L);

        // then
        verify(userQueryService, times(1)).findById(anyLong());
        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    @DisplayName("사용자 삭제(이메일 기반) 성공")
    void deleteUserByEmail_Success() {
        // given
        when(userRepository.findByEmail(anyString())).thenReturn(testUser);
        doNothing().when(userRepository).delete(any(User.class));

        // when
        userCommandService.deleteUser("test@example.com");

        // then
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    @DisplayName("사용자 삭제(이메일 기반) 실패 - 사용자 없음")
    void deleteUserByEmail_UserNotFound_ThrowsException() {
        // given
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        // when & then
        assertThrows(UserDomainException.class, () -> userCommandService.deleteUser("nonexistent@example.com"));
        
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, never()).delete(any(User.class));
    }
} 