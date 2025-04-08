package com.jwp.api.service;

import com.jwp.api.dto.request.UserCreateRequest;
import com.jwp.api.dto.request.UserUpdateRequest;
import com.jwp.api.dto.response.UserResponse;
import com.jwp.core.domain.User;
import com.jwp.core.repository.UserSearchCondition;
import com.jwp.core.service.UserCommandService;
import com.jwp.core.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 API 서비스
 * 컨트롤러와 도메인 서비스 계층 사이의 변환 작업을 담당합니다.
 */
@Service
@RequiredArgsConstructor
public class UserApiService {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    /**
     * 사용자 생성
     * @param command 사용자 생성 명령
     * @return 생성된 사용자 ID
     */
    @Transactional
    public Long createUser(UserCreateRequest.UserCreateCommand command) {
        User user = User.builder()
                .email(command.email())
                .name(command.name())
                .password(command.password())
                .build();
        
        User savedUser = userCommandService.createUser(user);
        return savedUser.getId();
    }

    /**
     * 사용자 조회
     * @param userId 사용자 ID
     * @return 사용자 정보
     */
    @Transactional(readOnly = true)
    public User findUser(Long userId) {
        return userQueryService.findById(userId);
    }

    /**
     * 사용자 목록 조회
     * @param condition 검색 조건
     * @param pageable 페이징 정보
     * @return 사용자 목록
     */
    @Transactional(readOnly = true)
    public Page<User> findUsers(UserSearchCondition condition, Pageable pageable) {
        return userQueryService.searchUsers(condition, pageable);
    }

    /**
     * 사용자 정보 수정
     * @param userId 사용자 ID
     * @param command 수정 명령
     * @return 수정된 사용자 정보
     */
    @Transactional
    public User updateUser(Long userId, UserUpdateRequest.UserUpdateCommand command) {
        User user = findUser(userId);
        return userCommandService.updateUserInfo(user.getEmail(), command.name());
    }

    /**
     * 사용자 삭제
     * @param userId 사용자 ID
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = findUser(userId);
        userCommandService.deleteUser(user.getEmail());
    }
} 