package com.jwp.core.service;

import com.jwp.core.domain.User;
import com.jwp.core.domain.UserStatus;
import com.jwp.core.exception.BusinessException;
import com.jwp.core.exception.user.UserDomainException;
import com.jwp.core.repository.UserRepository;
import com.jwp.core.repository.UserSearchCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserQueryService userQueryService;

    private User testUser;
    private Pageable pageable;

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
        
        // 페이징 정보 설정
        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("이메일로 사용자 조회 성공")
    void findByEmail_Success() {
        // given
        when(userRepository.findByEmail(anyString())).thenReturn(testUser);

        // when
        Optional<User> result = userQueryService.findByEmail("test@example.com");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
        assertThat(result.get().getName()).isEqualTo("테스트 사용자");
        
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("이메일로 사용자 조회 실패 - 사용자 없음")
    void findByEmail_UserNotFound() {
        // given
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        // when
        Optional<User> result = userQueryService.findByEmail("nonexistent@example.com");

        // then
        assertThat(result).isEmpty();
        
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("ID로 사용자 조회 성공")
    void findById_Success() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        // when
        User result = userQueryService.findById(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("ID로 사용자 조회 실패 - 사용자 없음")
    void findById_UserNotFound_ThrowsException() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserDomainException.class, () -> userQueryService.findById(999L));
        
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("이메일 존재 여부 확인")
    void existsByEmail_ReturnsTrueForExistingEmail() {
        // given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // when
        boolean exists = userQueryService.existsByEmail("test@example.com");

        // then
        assertThat(exists).isTrue();
        
        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    @DisplayName("이메일 존재 여부 확인 - 존재하지 않음")
    void existsByEmail_ReturnsFalseForNonExistingEmail() {
        // given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        // when
        boolean exists = userQueryService.existsByEmail("nonexistent@example.com");

        // then
        assertThat(exists).isFalse();
        
        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    @DisplayName("이름으로 사용자 검색 성공")
    void findByNameContaining_Success() {
        // given
        List<User> userList = Collections.singletonList(testUser);
        Page<User> userPage = new PageImpl<>(userList, pageable, 1);
        
        when(userRepository.findByNameContaining(anyString(), any(Pageable.class))).thenReturn(userPage);

        // when
        Page<User> result = userQueryService.findByNameContaining("테스트", pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).contains("테스트");
        
        verify(userRepository, times(1)).findByNameContaining(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("이름으로 사용자 검색 실패 - 빈 이름")
    void findByNameContaining_EmptyName_ThrowsException() {
        // when & then
        assertThrows(BusinessException.class, () -> userQueryService.findByNameContaining("", pageable));
        assertThrows(BusinessException.class, () -> userQueryService.findByNameContaining(null, pageable));
        
        verify(userRepository, never()).findByNameContaining(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("조건으로 사용자 검색 성공")
    void searchUsers_Success() {
        // given
        List<User> userList = Collections.singletonList(testUser);
        Page<User> userPage = new PageImpl<>(userList, pageable, 1);
        UserSearchCondition condition = UserSearchCondition.builder()
                .name("테스트")
                .build();
        
        when(userRepository.searchByCondition(any(UserSearchCondition.class), any(Pageable.class))).thenReturn(userPage);

        // when
        Page<User> result = userQueryService.searchUsers(condition, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).contains("테스트");
        
        verify(userRepository, times(1)).searchByCondition(any(UserSearchCondition.class), any(Pageable.class));
    }

    @Test
    @DisplayName("조건으로 사용자 검색 실패 - 페이징 정보 없음")
    void searchUsers_NullPageable_ThrowsException() {
        // given
        UserSearchCondition condition = UserSearchCondition.builder().build();

        // when & then
        assertThrows(BusinessException.class, () -> userQueryService.searchUsers(condition, null));
        
        verify(userRepository, never()).searchByCondition(any(UserSearchCondition.class), any(Pageable.class));
    }

    @Test
    @DisplayName("조건으로 사용자 검색 실패 - 검색 조건 없음")
    void searchUsers_NullCondition_ThrowsException() {
        // when & then
        assertThrows(BusinessException.class, () -> userQueryService.searchUsers(null, pageable));
        
        verify(userRepository, never()).searchByCondition(any(UserSearchCondition.class), any(Pageable.class));
    }
} 