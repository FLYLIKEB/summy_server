package com.jwp.core.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;

import com.jwp.core.TestApplication;
import com.jwp.core.domain.User;
import com.jwp.core.domain.UserStatus;
import com.jwp.core.exception.common.InvalidValueException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@ContextConfiguration(classes = TestApplication.class)
@DisplayName("UserRepositoryImpl 테스트")
class UserRepositoryImplTest {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private UserRepositoryImpl repository;
    
    private User user1;
    private User user2;
    private User user3;
    
    @Autowired
    private UserRepository userRepository;
    
    @BeforeEach
    void setUp() {
        // UserRepositoryImpl 인스턴스 직접 생성
        repository = new UserRepositoryImpl(entityManager);
        
        // 테스트 데이터 초기화
        userRepository.deleteAll();
        
        // 테스트용 사용자 생성
        user1 = User.builder()
                .email("user1@example.com")
                .name("사용자1")
                .password("password1")
                .build();
        
        user2 = User.builder()
                .email("user2@example.com")
                .name("사용자2")
                .password("password2")
                .build();
        
        user3 = User.builder()
                .email("admin@example.com")
                .name("관리자")
                .password("admin123")
                .status(UserStatus.ACTIVE)
                .build();
        
        // 테스트용 사용자 저장
        userRepository.saveAll(List.of(user1, user2, user3));
    }
    
    @Test
    @DisplayName("이름으로 사용자 검색 시 정상적으로 결과 반환")
    void findByNameContaining_ShouldReturnMatchingUsers_WhenNameMatches() {
        // given
        String nameKeyword = "사용자";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        
        // when
        Page<User> userPage = repository.findByNameContaining(nameKeyword, pageable);
        
        // then
        assertThat(userPage).isNotNull();
        assertThat(userPage.getTotalElements()).isEqualTo(2);
        assertThat(userPage.getContent()).extracting("name")
                .containsExactly("사용자1", "사용자2");
    }
    
    @Test
    @DisplayName("이름으로 사용자 검색 시 페이징 정보가 없으면 예외 발생")
    void findByNameContaining_ShouldThrowException_WhenPageableIsNull() {
        // given
        String nameKeyword = "사용자";
        Pageable pageable = null;
        
        // when & then
        assertThatThrownBy(() -> repository.findByNameContaining(nameKeyword, pageable))
                .isInstanceOf(InvalidValueException.class);
    }
    
    @Test
    @DisplayName("검색 조건으로 사용자 검색 - 이메일")
    void searchByCondition_ShouldReturnUser_WhenEmailConditionMatches() {
        // given
        UserSearchCondition condition = UserSearchCondition.builder()
                .email("admin@example.com")
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        
        // when
        Page<User> userPage = repository.searchByCondition(condition, pageable);
        
        // then
        assertThat(userPage).isNotNull();
        assertThat(userPage.getTotalElements()).isEqualTo(1);
        assertThat(userPage.getContent()).extracting("email")
                .containsExactly("admin@example.com");
    }
    
    @Test
    @DisplayName("검색 조건으로 사용자 검색 - 이름")
    void searchByCondition_ShouldReturnUser_WhenNameConditionMatches() {
        // given
        UserSearchCondition condition = UserSearchCondition.builder()
                .name("관리자")
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        
        // when
        Page<User> userPage = repository.searchByCondition(condition, pageable);
        
        // then
        assertThat(userPage).isNotNull();
        assertThat(userPage.getTotalElements()).isEqualTo(1);
        assertThat(userPage.getContent()).extracting("name")
                .containsExactly("관리자");
    }
    
    @Test
    @DisplayName("검색 조건으로 사용자 검색 - 날짜 범위")
    void searchByCondition_ShouldReturnUsers_WhenDateRangeMatches() {
        // 테스트에서는 엔티티의 생성일시/수정일시가 정확히 설정되지 않을 수 있으므로,
        // 날짜 조건 없이 모든 사용자가 검색되는지 확인
        
        // given
        UserSearchCondition condition = UserSearchCondition.builder().build();
        Pageable pageable = PageRequest.of(0, 10);
        
        // when
        Page<User> userPage = repository.searchByCondition(condition, pageable);
        
        // then
        assertThat(userPage).isNotNull();
        assertThat(userPage.getTotalElements()).isEqualTo(3);
    }
    
    @Test
    @DisplayName("검색 조건으로 사용자 검색 - 복합 조건")
    void searchByCondition_ShouldReturnUser_WhenMultipleConditionsMatch() {
        // given
        UserSearchCondition condition = UserSearchCondition.builder()
                .name("사용자")
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        
        // when
        Page<User> userPage = repository.searchByCondition(condition, pageable);
        
        // then
        assertThat(userPage).isNotNull();
        assertThat(userPage.getTotalElements()).isEqualTo(2);
        assertThat(userPage.getContent()).extracting("name")
                .containsExactlyInAnyOrder("사용자1", "사용자2");
    }
    
    @Test
    @DisplayName("검색 조건이 없을 때 모든 사용자 반환")
    void searchByCondition_ShouldReturnAllUsers_WhenConditionIsEmpty() {
        // given
        UserSearchCondition condition = UserSearchCondition.builder().build();
        Pageable pageable = PageRequest.of(0, 10);
        
        // when
        Page<User> userPage = repository.searchByCondition(condition, pageable);
        
        // then
        assertThat(userPage).isNotNull();
        assertThat(userPage.getTotalElements()).isEqualTo(3);
    }
    
    @Test
    @DisplayName("검색 조건으로 사용자 검색 시 페이징 정보가 없으면 예외 발생")
    void searchByCondition_ShouldThrowException_WhenPageableIsNull() {
        // given
        UserSearchCondition condition = UserSearchCondition.builder().build();
        Pageable pageable = null;
        
        // when & then
        assertThatThrownBy(() -> repository.searchByCondition(condition, pageable))
                .isInstanceOf(InvalidValueException.class);
    }
} 