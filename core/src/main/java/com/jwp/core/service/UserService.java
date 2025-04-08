package com.jwp.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwp.core.domain.User;
import com.jwp.core.repository.UserSearchCondition;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * 사용자 서비스 Facade
 * CQRS 기반의 Command 및 Query 서비스를 통합적으로 제공합니다.
 * 
 * @deprecated 직접 UserCommandService 혹은 UserQueryService를 사용하는 것을 권장합니다.
 */
@Service
@Deprecated
@RequiredArgsConstructor
public class UserService {
    
    private final UserCommandService commandService;
    private final UserQueryService queryService;
    
    /**
     * 이메일로 사용자 조회
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return queryService.findByEmail(email);
    }
    
    /**
     * 사용자 ID로 조회
     */
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return queryService.findById(id);
    }
    
    /**
     * 이메일 중복 확인
     */
    @Transactional(readOnly = true)
    public boolean isDuplicateEmail(String email) {
        return queryService.existsByEmail(email);
    }
    
    /**
     * 이름으로 사용자 검색
     */
    @Transactional(readOnly = true)
    public Page<User> findByNameContaining(String name, Pageable pageable) {
        return queryService.findByNameContaining(name, pageable);
    }
    
    /**
     * 조건에 따른 사용자 검색
     */
    @Transactional(readOnly = true)
    public Page<User> searchUsers(UserSearchCondition condition, Pageable pageable) {
        return queryService.searchUsers(condition, pageable);
    }
    
    /**
     * 사용자 생성
     */
    @Transactional
    public User createUser(@Valid User user) {
        return commandService.createUser(user);
    }
    
    /**
     * 사용자 정보 업데이트
     */
    @Transactional
    public User updateUserInfo(String email, String newName) {
        return commandService.updateUserInfo(email, newName);
    }
    
    /**
     * 비밀번호 변경
     */
    @Transactional
    public User changePassword(String email, String newPassword) {
        return commandService.changePassword(email, newPassword);
    }
    
    /**
     * 사용자 삭제
     */
    @Transactional
    public void deleteUser(String email) {
        commandService.deleteUser(email);
    }
} 