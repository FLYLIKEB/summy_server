package com.jwp.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.jwp.core.domain.User;
import com.jwp.core.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 서비스
 * 사용자 관련 비즈니스 로직을 처리합니다.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    /**
     * 이메일로 사용자 조회
     * @param email 조회할 이메일
     * @return 조회된 사용자 (Optional)
     */
    public Optional<User> findByEmail(String email) {
        Assert.hasText(email, "이메일은 필수입니다.");
        
        User user = userRepository.findByEmail(email);
        return Optional.ofNullable(user);
    }
    
    /**
     * 이메일 중복 확인
     * @param email 확인할 이메일
     * @return 중복 여부
     */
    public boolean isDuplicateEmail(String email) {
        Assert.hasText(email, "이메일은 필수입니다.");
        return userRepository.existsByEmail(email);
    }
    
    /**
     * 사용자 생성
     * @param user 생성할 사용자 정보
     * @return 생성된 사용자
     * @throws IllegalArgumentException 이메일이 중복된 경우
     */
    @Transactional
    public User createUser(@Valid User user) {
        Assert.notNull(user, "사용자 정보는 필수입니다.");
        
        if (isDuplicateEmail(user.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        
        return userRepository.save(user);
    }
    
    /**
     * 사용자 정보 업데이트
     * @param email 대상 사용자 이메일
     * @param newName 새로운 이름
     * @return 업데이트된 사용자
     * @throws IllegalArgumentException 사용자를 찾을 수 없는 경우
     */
    @Transactional
    public User updateUserInfo(String email, String newName) {
        Assert.hasText(email, "이메일은 필수입니다.");
        Assert.hasText(newName, "이름은 필수입니다.");
        
        return findByEmail(email)
                .map(user -> {
                    user.update(newName); // update 메서드 사용
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
    
    /**
     * 비밀번호 변경
     * @param email 대상 사용자 이메일
     * @param newPassword 새로운 비밀번호
     * @return 업데이트된 사용자
     * @throws IllegalArgumentException 사용자를 찾을 수 없는 경우
     */
    @Transactional
    public User changePassword(String email, String newPassword) {
        Assert.hasText(email, "이메일은 필수입니다.");
        Assert.hasText(newPassword, "비밀번호는 필수입니다.");
        
        return findByEmail(email)
                .map(user -> {
                    user.changePassword(newPassword); // changePassword 메서드 사용
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
    
    /**
     * 사용자 삭제
     * @param email 삭제할 사용자 이메일
     * @throws IllegalArgumentException 사용자를 찾을 수 없는 경우
     */
    @Transactional
    public void deleteUser(String email) {
        Assert.hasText(email, "이메일은 필수입니다.");
        
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        
        userRepository.delete(user);
    }
} 