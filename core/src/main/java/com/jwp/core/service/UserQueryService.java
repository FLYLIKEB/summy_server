package com.jwp.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwp.core.domain.User;
import com.jwp.core.exception.BusinessException;
import com.jwp.core.exception.ErrorCode;
import com.jwp.core.exception.user.UserDomainException;
import com.jwp.core.repository.UserRepository;
import com.jwp.core.repository.UserSearchCondition;

import java.util.Optional;

/**
 * 사용자 조회 서비스
 * 읽기 전용 작업을 처리하는 CQRS의 Query 부분을 담당합니다.
 */
@Service
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;

    /**
     * 생성자
     * @param userRepository 사용자 레포지토리
     */
    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 이메일로 사용자 조회
     * @param email 조회할 이메일
     * @return 조회된 사용자 (Optional)
     */
    public Optional<User> findByEmail(String email) {
        validateEmail(email);
        User user = userRepository.findByEmail(email);
        return Optional.ofNullable(user);
    }

    /**
     * 사용자 ID로 조회
     * @param id 사용자 ID
     * @return 조회된 사용자
     */
    public User findById(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "사용자 ID는 필수입니다.");
        }

        return userRepository.findById(id)
                .orElseThrow(() -> UserDomainException.userNotFound("ID: " + id));
    }

    /**
     * 이메일 중복 확인
     * @param email 확인할 이메일
     * @return 중복 여부
     */
    public boolean existsByEmail(String email) {
        validateEmail(email);
        return userRepository.existsByEmail(email);
    }

    /**
     * 사용자 이름으로 검색
     * @param name 검색할 이름
     * @param pageable 페이징 정보
     * @return 페이징된 사용자 목록
     */
    public Page<User> findByNameContaining(String name, Pageable pageable) {
        if (name == null || name.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "검색할 이름은 필수입니다.");
        }

        return userRepository.findByNameContaining(name, pageable);
    }

    /**
     * 조건에 따른 사용자 검색
     * @param condition 검색 조건
     * @param pageable 페이징 정보
     * @return 페이징된 사용자 목록
     */
    public Page<User> searchUsers(UserSearchCondition condition, Pageable pageable) {
        if (pageable == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "페이징 정보는 필수입니다.");
        }

        return userRepository.searchByCondition(condition, pageable);
    }

    // 유효성 검증 메소드
    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이메일은 필수입니다.");
        }
    }
}
