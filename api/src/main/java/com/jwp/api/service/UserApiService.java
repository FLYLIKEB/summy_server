package com.jwp.api.service;

import com.jwp.api.dto.request.UserCreateRequest;
import com.jwp.api.dto.request.UserUpdateRequest;
import com.jwp.api.dto.response.UserResponse;
import com.jwp.core.domain.User;
import com.jwp.core.exception.BusinessException;
import com.jwp.core.exception.ErrorCode;
import com.jwp.core.exception.user.UserDomainException;
import com.jwp.core.repository.UserSearchCondition;
import com.jwp.core.service.UserCommandService;
import com.jwp.core.service.UserQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 API 서비스
 * 컨트롤러와 도메인 서비스 계층 사이의 변환 작업을 담당합니다.
 */
@Service
public class UserApiService {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    
    /**
     * 생성자
     * @param userCommandService 사용자 명령 서비스
     * @param userQueryService 사용자 조회 서비스
     */
    public UserApiService(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    /**
     * 사용자 생성
     * @param command 사용자 생성 명령
     * @return 생성된 사용자 ID
     * @throws BusinessException 이메일 중복 등의 비즈니스 규칙 위반 시
     */
    @Transactional
    public Long createUser(UserCreateRequest.UserCreateCommand command) {
        if (command == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "사용자 생성 명령은 필수입니다.");
        }

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
     * @throws BusinessException 사용자를 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public User findUser(Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "사용자 ID는 필수입니다.");
        }
        
        try {
            return userQueryService.findById(userId);
        } catch (BusinessException e) {
            // 로깅 추가 (로깅 프레임워크 사용 권장)
            System.err.println("사용자 조회 실패: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 사용자 목록 조회
     * @param condition 검색 조건
     * @param pageable 페이징 정보
     * @return 사용자 목록
     * @throws BusinessException 유효하지 않은 검색 조건이나 페이징 정보
     */
    @Transactional(readOnly = true)
    public Page<User> findUsers(UserSearchCondition condition, Pageable pageable) {
        if (condition == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "검색 조건은 필수입니다.");
        }
        
        if (pageable == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "페이징 정보는 필수입니다.");
        }
        
        try {
            return userQueryService.searchUsers(condition, pageable);
        } catch (Exception e) {
            // 로깅 추가 (로깅 프레임워크 사용 권장)
            System.err.println("사용자 목록 조회 실패: " + e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "사용자 목록 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * 사용자 정보 수정
     * @param userId 사용자 ID
     * @param command 수정 명령
     * @return 수정된 사용자 정보
     * @throws BusinessException 사용자를 찾을 수 없는 경우
     */
    @Transactional
    public User updateUser(Long userId, UserUpdateRequest.UserUpdateCommand command) {
        if (command == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "수정 명령은 필수입니다.");
        }
        
        User user = findUser(userId);
        return userCommandService.updateUserInfo(user.getEmail(), command.name());
    }

    /**
     * 사용자 삭제
     * @param userId 사용자 ID
     * @throws BusinessException 사용자를 찾을 수 없는 경우
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = findUser(userId);
        userCommandService.deleteUser(user.getEmail());
    }
} 