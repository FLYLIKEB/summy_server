package com.jwp.api.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import com.jwp.api.service.UserApiService;
import com.jwp.core.domain.User;
import com.jwp.core.domain.UserStatus;
import com.jwp.core.repository.UserSearchCondition;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.common.mapper.TypeRef;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserAcceptanceTest {
    @LocalServerPort
    private int port;

    @MockBean
    private UserApiService userApiService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        
        // Mock 데이터 설정
        User user1 = User.builder()
            .email("user1@example.com")
            .name("사용자1")
            .password("password1")
            .status(UserStatus.ACTIVE)
            .build();
        ReflectionTestUtils.setField(user1, "id", 1L);
            
        User user2 = User.builder()
            .email("user2@example.com")
            .name("사용자2")
            .password("password2")
            .status(UserStatus.ACTIVE)
            .build();
        ReflectionTestUtils.setField(user2, "id", 2L);
            
        // Mock 동작 설정
        when(userApiService.findUsers(any(UserSearchCondition.class), any(Pageable.class)))
            .thenReturn(new PageImpl<>(Arrays.asList(user1, user2)));
            
        when(userApiService.findUser(any(Long.class)))
            .thenReturn(user1);
    }

    /**
     * 테스트를 위한 사용자 생성
     *
     * @param email 사용자 이메일
     * @param name 사용자 이름
     */
    private void createTestUser(String email, String name) {
        // 사용자 생성 로직 구현
        // 예: 사용자 등록 API 호출
    }

    /**
     * GET 요청을 보내는 유틸리티 메소드
     *
     * @param url 요청 URL
     * @param queryParams 쿼리 파라미터
     * @return 응답 객체
     */
    private ExtractableResponse<Response> 요청_GET(String url, Map<String, Object> queryParams) {
        System.out.println("Executing GET request to: " + url + " with params: " + queryParams);
        return RestAssured.given()
            .log().all()
            .queryParams(queryParams)
            .when()
            .get(url)
            .then()
            .log().all()
            .extract();
    }

    /**
     * URL 생성 유틸리티 메소드
     *
     * @param path 경로
     * @return 전체 URL
     */
    private String getUrl(String path) {
        // URL 생성 로직 구현
        return path;
    }

    /**
     * 응답 상태 코드 검증 유틸리티 메소드
     *
     * @param response 응답 객체
     * @param expectedStatusCode 기대하는 상태 코드
     */
    private void 응답_상태코드_검증(ExtractableResponse<Response> response, int expectedStatusCode) {
        assertThat(response.statusCode()).isEqualTo(expectedStatusCode);
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

        ExtractableResponse<Response> response = 요청_GET(getUrl("/api/v1/users"), queryParams);

        // then
        응답_상태코드_검증(response, HttpStatus.OK.value());

        // 결과 확인 - 타입 안전한 변환 방식 사용
        Map<String, Object> responseBody = response.body().as(new TypeRef<Map<String, Object>>() {});
        
        // 스트림을 활용한 안전한 변환
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> content = responseBody.get("content") != null ? 
            ((List<?>) responseBody.get("content")).stream()
                .map(item -> (Map<String, Object>) item)
                .collect(Collectors.toList()) : 
            Collections.emptyList();
        
        assertThat(responseBody.get("totalElements")).isEqualTo(2);
        assertThat(content).hasSize(2);
        assertThat(content).extracting("name").containsExactlyInAnyOrder("사용자1", "사용자2");
    }
} 