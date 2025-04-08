package com.jwp.core.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자 엔티티
 * 시스템 사용자 정보를 관리하는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    /**
     * 사용자 생성자 (빌더 패턴 사용)
     */
    @Builder
    private User(String email, String name, String password, UserStatus status) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.status = status != null ? status : UserStatus.ACTIVE;
    }

    /**
     * 사용자 정보 업데이트
     */
    public void update(String name) {
        this.name = name;
    }

    /**
     * 비밀번호 변경
     */
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * 로그인 시간 업데이트
     */
    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }

    /**
     * 사용자 상태 변경
     */
    public void changeStatus(UserStatus status) {
        this.status = status;
    }
}
