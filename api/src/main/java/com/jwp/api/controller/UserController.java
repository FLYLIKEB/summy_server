package com.jwp.api.controller;

import com.jwp.api.dto.request.UserCreateRequest;
import com.jwp.api.dto.request.UserUpdateRequest;
import com.jwp.api.dto.response.UserResponse;
import com.jwp.api.service.UserApiService;
import com.jwp.core.domain.User;
import com.jwp.core.repository.UserSearchCondition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/users")
@Tag(name = "사용자 관리", description = "사용자 CRUD API")
public class UserController {

    private final UserApiService userApiService;
    
    /**
     * 생성자
     * @param userApiService 사용자 API 서비스
     */
    public UserController(UserApiService userApiService) {
        this.userApiService = userApiService;
    }

    /**
     * 사용자 생성 API
     * @param request 사용자 생성 요청 정보
     * @return 생성된 사용자 정보
     */
    @PostMapping
    @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "사용자 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "409", description = "이메일 중복", content = @Content)
    })
    public ResponseEntity<Void> createUser(
            @RequestBody @Valid @Parameter(description = "사용자 생성 정보", required = true) UserCreateRequest request) {
        Long userId = userApiService.createUser(request.toCommand());
        return ResponseEntity.created(URI.create("/api/v1/users/" + userId)).build();
    }

    /**
     * 사용자 조회 API
     * @param userId 사용자 ID
     * @return 사용자 정보
     */
    @GetMapping("/{userId}")
    @Operation(summary = "사용자 조회", description = "ID로 사용자를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자 조회 성공"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<UserResponse> getUser(
            @PathVariable @Parameter(description = "사용자 ID", required = true) Long userId) {
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
    @Operation(summary = "사용자 목록 조회", description = "조건에 맞는 사용자 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    public ResponseEntity<Page<UserResponse>> getUsers(
            @RequestParam(required = false) @Parameter(description = "이메일 검색") String email,
            @RequestParam(required = false) @Parameter(description = "이름 검색") String name,
            @Parameter(description = "페이징 정보") Pageable pageable) {
        
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
    @Operation(summary = "사용자 정보 수정", description = "사용자 정보를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable @Parameter(description = "사용자 ID", required = true) Long userId,
            @RequestBody @Valid @Parameter(description = "수정 정보", required = true) UserUpdateRequest request) {
        
        User user = userApiService.updateUser(userId, request.toCommand());
        return ResponseEntity.ok(UserResponse.from(user));
    }

    /**
     * 사용자 삭제 API
     * @param userId 사용자 ID
     * @return 응답 엔티티
     */
    @DeleteMapping("/{userId}")
    @Operation(summary = "사용자 삭제", description = "사용자를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "사용자 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<Void> deleteUser(
            @PathVariable @Parameter(description = "사용자 ID", required = true) Long userId) {
        userApiService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}