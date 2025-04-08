package com.jwp.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwp.core.domain.User;
import com.jwp.core.repository.UserRepository;

import jakarta.validation.Valid;
import java.util.Objects;
import java.util.Optional;

/**
 * 사용자 서비스
 * 사용자 관련 비즈니스 로직을 처리합니다.
 */
@Service
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    
    /**
     * 사용자 서비스 생성자
     * @param userRepository 사용자 레포지토리
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository must not be null");
    }
    
    /**
     * 이메일로 사용자 조회
     * @param email 조회할 이메일
     * @return 사용자 Optional (존재하지 않을 경우 empty)
     * @throws IllegalArgumentException 이메일이 유효하지 않은 경우
     */
    public Optional<User> findByEmail(String email) {
        validateEmail(email);
        return Optional.ofNullable(userRepository.findByEmail(email));
    }
    
    /**
     * 사용자 생성
     * @param user 생성할 사용자 정보
     * @return 생성된 사용자
     * @throws IllegalArgumentException 이미 존재하는 이메일이거나 유효하지 않은 사용자 정보인 경우
     */
    @Transactional
    public User createUser(@Valid User user) {
        Objects.requireNonNull(user, "사용자 정보는 null일 수 없습니다");
        validateNewUser(user);
        return userRepository.save(user);
    }

    /**
     * 사용자 이름 업데이트
     * @param email 업데이트할 사용자 이메일
     * @param newName 새로운 이름
     * @return 업데이트된 사용자
     * @throws IllegalArgumentException 사용자가 존재하지 않거나 이름이 유효하지 않은 경우
     */
    @Transactional
    public User updateUserName(String email, String newName) {
        validateEmail(email);
        Objects.requireNonNull(newName, "새 이름은 null일 수 없습니다");
        
        return findByEmail(email)
                .map(user -> {
                    user.updateName(newName);
                    return user;
                })
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자가 존재하지 않습니다: " + email));
    }
    
    /**
     * 사용자 비밀번호 업데이트
     * @param email 업데이트할 사용자 이메일
     * @param newPassword 새로운 비밀번호
     * @return 업데이트된 사용자
     * @throws IllegalArgumentException 사용자가 존재하지 않거나 비밀번호가 유효하지 않은 경우
     */
    @Transactional
    public User updateUserPassword(String email, String newPassword) {
        validateEmail(email);
        Objects.requireNonNull(newPassword, "새 비밀번호는 null일 수 없습니다");
        
        return findByEmail(email)
                .map(user -> {
                    user.updatePassword(newPassword);
                    return user;
                })
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자가 존재하지 않습니다: " + email));
    }
    
    /**
     * 사용자 삭제
     * @param email 삭제할 사용자 이메일
     * @throws IllegalArgumentException 사용자가 존재하지 않는 경우
     */
    @Transactional
    public void deleteUser(String email) {
        validateEmail(email);
        
        User user = findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자가 존재하지 않습니다: " + email));
        
        userRepository.delete(user);
    }

    /**
     * 신규 사용자 유효성 검증
     * @param user 검증할 사용자
     * @throws IllegalArgumentException 이미 존재하는 이메일인 경우
     */
    private void validateNewUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + user.getEmail());
        }
    }

    /**
     * 이메일 유효성 검증
     * @param email 검증할 이메일
     * @throws IllegalArgumentException 이메일이 유효하지 않은 경우
     */
    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수입니다");
        }
    }
} 