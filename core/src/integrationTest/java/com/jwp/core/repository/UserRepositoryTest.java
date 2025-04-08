package com.jwp.core.repository;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.jwp.core.TestApplication;
import com.jwp.core.domain.User;
import com.jwp.core.domain.UserStatus;

@DataJpaTest
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("test")
@DisplayName("UserRepository 테스트")
class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserRepositoryCustom userRepositoryCustom;
    
    private User user1;
    private User user2;
    private User user3;
    
    @BeforeEach
    void setUp() {
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
                .status(UserStatus.ADMIN)
                .build();
        
        // 테스트용 사용자 저장
        userRepository.saveAll(List.of(user1, user2, user3));
    }
    
    @Test
    @DisplayName("이메일로 사용자 조회")
    void findByEmail_ShouldReturnUser_WhenEmailExists() {
        // given
        String email = "user1@example.com";
        
        // when
        User foundUser = userRepository.findByEmail(email);
        
        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(email);
        assertThat(foundUser.getName()).isEqualTo("사용자1");
    }
    
    @Test
    @DisplayName("존재하지 않는 이메일로 조회 시 null 반환")
    void findByEmail_ShouldReturnNull_WhenEmailDoesNotExist() {
        // given
        String nonExistentEmail = "nonexistent@example.com";
        
        // when
        User foundUser = userRepository.findByEmail(nonExistentEmail);
        
        // then
        assertThat(foundUser).isNull();
    }
    
    @Test
    @DisplayName("이메일 존재 여부 확인")
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        // given
        String email = "user2@example.com";
        
        // when
        boolean exists = userRepository.existsByEmail(email);
        
        // then
        assertThat(exists).isTrue();
    }
    
    @Test
    @DisplayName("사용자 이름으로 검색")
    void findByNameContaining_ShouldReturnMatchingUsers() {
        // given
        String nameKeyword = "사용자";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        
        // when
        Page<User> userPage = ((UserRepositoryImpl) userRepositoryCustom).findByNameContaining(nameKeyword, pageable);
        
        // then
        assertThat(userPage).isNotNull();
        assertThat(userPage.getContent()).hasSize(2);
        assertThat(userPage.getContent()).extracting("name")
                .containsExactly("사용자1", "사용자2");
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
        Page<User> userPage = userRepositoryCustom.searchByCondition(condition, pageable);
        
        // then
        assertThat(userPage).isNotNull();
        assertThat(userPage.getContent()).hasSize(1);
        assertThat(userPage.getContent().get(0).getEmail()).isEqualTo("admin@example.com");
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
        Page<User> userPage = userRepositoryCustom.searchByCondition(condition, pageable);
        
        // then
        assertThat(userPage).isNotNull();
        assertThat(userPage.getContent()).hasSize(1);
        assertThat(userPage.getContent().get(0).getName()).isEqualTo("관리자");
    }
    
    @Test
    @DisplayName("검색 조건으로 사용자 검색 - 날짜 범위")
    void searchByCondition_ShouldReturnUsers_WhenDateRangeMatches() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayAgo = now.minusDays(1);
        UserSearchCondition condition = UserSearchCondition.builder()
                .fromDate(oneDayAgo)
                .toDate(now.plusDays(1))
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        
        // when
        Page<User> userPage = userRepositoryCustom.searchByCondition(condition, pageable);
        
        // then
        assertThat(userPage).isNotNull();
        assertThat(userPage.getContent()).hasSize(3); // 모든 사용자가 해당 날짜 범위에 생성됨
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
        Page<User> userPage = userRepositoryCustom.searchByCondition(condition, pageable);
        
        // then
        assertThat(userPage).isNotNull();
        assertThat(userPage.getContent()).hasSize(2);
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
        Page<User> userPage = userRepositoryCustom.searchByCondition(condition, pageable);
        
        // then
        assertThat(userPage).isNotNull();
        assertThat(userPage.getContent()).hasSize(3);
    }
} 