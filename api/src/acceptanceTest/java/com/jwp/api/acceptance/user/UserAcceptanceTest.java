package com.jwp.api.acceptance.user;

import com.jwp.api.acceptance.AcceptanceTest;
import com.jwp.api.acceptance.DatabaseCleanup;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.jwp.api.acceptance.AcceptanceSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 사용자 API 인수 테스트
 * 사용자 관련 API의 End-to-End 테스트를 수행합니다.
 */
@DisplayName("사용자 API 인수 테스트")
class UserAcceptanceTest extends AcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    /**
     * 테스트 데이터 초기화
     * 모든 테스트 실행 전 데이터베이스를 초기화합니다.
     */
    @Override
    protected void initializeTestData() {
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("사용자 가입 성공 테스트")
    void registerUser_Success() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("email", "test@example.com");
        params.put("name", "테스트사용자");
        params.put("password", "password123");

        // when
        ExtractableResponse<Response> response = 요청_POST(getUrl("/api/users"), params);

        // then
        응답_상태코드_검증(response, HttpStatus.CREATED.value());

        // 응답에서 사용자 ID 확인
        String location = response.header("Location");
        assertThat(location).isNotNull();

        // 생성된 사용자 조회
        ExtractableResponse<Response> userResponse = 요청_GET(location);
        응답_상태코드_검증(userResponse, HttpStatus.OK.value());

        Map<String, Object> responseBody = userResponse.body().as(Map.class);
        assertThat(responseBody.get("email")).isEqualTo("test@example.com");
        assertThat(responseBody.get("name")).isEqualTo("테스트사용자");
    }

    @Test
    @DisplayName("사용자 정보 수정 테스트")
    void updateUser_Success() {
        // given
        // 1. 사용자 생성
        Map<String, Object> createParams = new HashMap<>();
        createParams.put("email", "update@example.com");
        createParams.put("name", "수정전사용자");
        createParams.put("password", "password123");

        ExtractableResponse<Response> createResponse = 요청_POST(getUrl("/api/users"), createParams);
        String location = createResponse.header("Location");

        // 2. 수정할 정보 준비
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("name", "수정후사용자");

        // when
        ExtractableResponse<Response> updateResponse = 요청_PUT(location, updateParams);

        // then
        응답_상태코드_검증(updateResponse, HttpStatus.OK.value());

        // 수정된 사용자 조회
        ExtractableResponse<Response> userResponse = 요청_GET(location);
        Map<String, Object> responseBody = userResponse.body().as(Map.class);
        assertThat(responseBody.get("name")).isEqualTo("수정후사용자");
    }

    @Test
    @DisplayName("사용자 검색 테스트")
    void searchUsers_Success() {
        // given
        // 여러 사용자 생성
        createTestUser("user1@example.com", "사용자1");
        createTestUser("user2@example.com", "사용자2");
        createTestUser("admin@example.com", "관리자");

        // when
        // 이름에 "사용자"가 포함된 사용자 검색
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("name", "사용자");

        ExtractableResponse<Response> response = 요청_GET(getUrl("/api/users"), queryParams);

        // then
        응답_상태코드_검증(response, HttpStatus.OK.value());

        // 결과 확인
        Map<String, Object> responseBody = response.body().as(Map.class);
        assertThat(responseBody.get("totalElements")).isEqualTo(2);
    }

    /**
     * 테스트 사용자 생성
     *
     * @param email 이메일
     * @param name 이름
     * @return 생성된 사용자의 API 응답
     */
    private ExtractableResponse<Response> createTestUser(String email, String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("name", name);
        params.put("password", "password123");

        return 요청_POST(getUrl("/api/users"), params);
    }
}
