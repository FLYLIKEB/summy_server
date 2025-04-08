package com.jwp.api.dto.response;

import com.jwp.core.domain.User;
import com.jwp.core.domain.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 사용자 응답 DTO
 * 사용자 API 응답 데이터를 담는 객체입니다.
 */
@Getter
@Builder
public class UserResponse {
    private final Long id;
    private final String email;
    private final String name;
    private final UserStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    /**
     * 도메인 객체로부터 응답 DTO 생성
     * @param user 사용자 도메인 객체
     * @return UserResponse 객체
     */
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
} 