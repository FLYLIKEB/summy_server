package com.jwp.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 생성 요청 DTO
 * 사용자 생성 API 요청 데이터를 담는 객체입니다.
 */
@Getter
@NoArgsConstructor
public class UserCreateRequest {

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하로 입력해주세요.")
    private String name;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 8, max = 100, message = "비밀번호는 8자 이상 100자 이하로 입력해주세요.")
    private String password;

    /**
     * 생성자
     * @param email 이메일
     * @param name 이름
     * @param password 비밀번호
     */
    public UserCreateRequest(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    /**
     * 서비스 레이어의 Command 객체로 변환
     * @return UserCreateCommand 객체
     */
    public UserCreateCommand toCommand() {
        return new UserCreateCommand(email, name, password);
    }

    /**
     * 사용자 생성 명령 객체
     * 서비스 레이어로 전달하는 명령 객체입니다.
     */
    public record UserCreateCommand(String email, String name, String password) {
    }
} 