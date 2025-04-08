package com.jwp.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwp.core.domain.User;
import com.jwp.core.exception.BusinessException;
import com.jwp.core.exception.ErrorCode;
import com.jwp.core.exception.user.UserDomainException;
import com.jwp.core.repository.UserRepository;

import jakarta.validation.Valid;

/**
 * 사용자 명령 서비스
 * 상태 변경 작업을 처리하는 CQRS의 Command 부분을 담당합니다.
 */
@Service
@Transactional
public class UserCommandService {
    
    private final UserRepository userRepository;
    private final UserQueryService userQueryService;
    
    /**
     * 생성자
     * @param userRepository 사용자 레포지토리
     * @param userQueryService 사용자 조회 서비스
     */
    public UserCommandService(UserRepository userRepository, UserQueryService userQueryService) {
        this.userRepository = userRepository;
        this.userQueryService = userQueryService;
    }
    
    /**
     * 사용자 생성
     * @param user 생성할 사용자 정보
     * @return 생성된 사용자
     */
    public User createUser(@Valid User user) {
        if (user == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "사용자 정보는 필수입니다.");
        }
        
        if (userQueryService.existsByEmail(user.getEmail())) {
            throw UserDomainException.emailDuplication(user.getEmail());
        }
        
        return userRepository.save(user);
    }
    
    /**
     * 사용자 정보 업데이트
     * @param email 대상 사용자 이메일
     * @param newName 새로운 이름
     * @return 업데이트된 사용자
     */
    public User updateUserInfo(String email, String newName) {
        validateEmail(email);
        validateName(newName);
        
        return userQueryService.findByEmail(email)
                .map(user -> {
                    user.update(newName);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> UserDomainException.userNotFound(email));
    }
    
    /**
     * 비밀번호 변경
     * @param email 대상 사용자 이메일
     * @param newPassword 새로운 비밀번호
     * @return 업데이트된 사용자
     */
    public User changePassword(String email, String newPassword) {
        validateEmail(email);
        validatePassword(newPassword);
        
        return userQueryService.findByEmail(email)
                .map(user -> {
                    user.changePassword(newPassword);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> UserDomainException.userNotFound(email));
    }
    
    /**
     * 사용자 삭제
     * @param email 삭제할 사용자 이메일
     */
    public void deleteUser(String email) {
        validateEmail(email);
        
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw UserDomainException.userNotFound(email);
        }
        
        userRepository.delete(user);
    }
    
    // 유효성 검증 메소드
    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이메일은 필수입니다.");
        }
    }
    
    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이름은 필수입니다.");
        }
    }
    
    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "비밀번호는 필수입니다.");
        }
    }
} 