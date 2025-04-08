package com.jwp.api.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보 수정 요청 DTO
 * 사용자 정보 수정 API 요청 데이터를 담는 객체입니다.
 */
@Getter
@NoArgsConstructor
public class UserUpdateRequest {

    @Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하로 입력해주세요.")
    private String name;

    /**
     * 생성자
     * @param name 변경할 이름
     */
    public UserUpdateRequest(String name) {
        this.name = name;
    }

    /**
     * 서비스 레이어의 Command 객체로 변환
     * @return UserUpdateCommand 객체
     */
    public UserUpdateCommand toCommand() {
        return new UserUpdateCommand(name);
    }

    /**
     * 사용자 정보 수정 명령 객체
     * 서비스 레이어로 전달하는 명령 객체입니다.
     */
    public record UserUpdateCommand(String name) {
    }
} 