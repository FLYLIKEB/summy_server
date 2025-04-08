package com.jwp.api.controller;

import com.jwp.api.dto.request.UserCreateRequest;
import com.jwp.api.dto.request.UserUpdateRequest;
import com.jwp.api.dto.response.UserResponse;
import com.jwp.api.service.UserApiService;
import com.jwp.core.domain.User;
import com.jwp.core.repository.UserSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.net.URI;

/**
 * 사용자 API 컨트롤러
 * 사용자 관련 API 엔드포인트를 제공합니다.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserApiService userApiService;

    /**
     * 사용자 생성 API
     * @param request 사용자 생성 요청 정보
     * @return 생성된 사용자 정보
     */
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserCreateRequest request) {
        Long userId = userApiService.createUser(request.toCommand());
        return ResponseEntity.created(URI.create("/api/users/" + userId)).build();
    }

    /**
     * 사용자 조회 API
     * @param userId 사용자 ID
     * @return 사용자 정보
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        User user = userApiService.findUser(userId);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    /**
     * 사용자 목록 조회 API
     * @param email 이메일 검색 조건 (선택)
     * @param name 이름 검색 조건 (선택)
     * @param pageable 페이징 정보
     * @return 사용자 목록
     */
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name,
            Pageable pageable) {
        
        UserSearchCondition condition = UserSearchCondition.builder()
                .email(email)
                .name(name)
                .build();
        
        Page<User> users = userApiService.findUsers(condition, pageable);
        return ResponseEntity.ok(users.map(UserResponse::from));
    }

    /**
     * 사용자 정보 수정 API
     * @param userId 사용자 ID
     * @param request 수정 요청 정보
     * @return 수정된 사용자 정보
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long userId,
            @RequestBody @Valid UserUpdateRequest request) {
        
        User user = userApiService.updateUser(userId, request.toCommand());
        return ResponseEntity.ok(UserResponse.from(user));
    }

    /**
     * 사용자 삭제 API
     * @param userId 사용자 ID
     * @return 응답 엔티티
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userApiService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
} 