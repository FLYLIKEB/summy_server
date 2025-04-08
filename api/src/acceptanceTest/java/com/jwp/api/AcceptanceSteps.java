package com.jwp.api;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 인수 테스트 단계 유틸리티
 * 인수 테스트에서 자주 사용되는 HTTP 요청/응답 단계를 정의합니다.
 */
public class AcceptanceSteps {

    /**
     * GET 요청 수행
     * @param uri 요청 URI
     * @return API 응답
     */
    public static ExtractableResponse<Response> 요청_GET(String uri) {
        return given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    /**
     * 쿼리 파라미터를 포함한 GET 요청 수행
     * @param uri 요청 URI
     * @param queryParams 쿼리 파라미터
     * @return API 응답
     */
    public static ExtractableResponse<Response> 요청_GET(String uri, Map<String, Object> queryParams) {
        return given().log().all()
                .queryParams(queryParams)
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    /**
     * POST 요청 수행
     * @param uri 요청 URI
     * @param params 요청 바디
     * @return API 응답
     */
    public static ExtractableResponse<Response> 요청_POST(String uri, Object params) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post(uri)
                .then().log().all()
                .extract();
    }

    /**
     * PUT 요청 수행
     * @param uri 요청 URI
     * @param params 요청 바디
     * @return API 응답
     */
    public static ExtractableResponse<Response> 요청_PUT(String uri, Object params) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }

    /**
     * DELETE 요청 수행
     * @param uri 요청 URI
     * @return API 응답
     */
    public static ExtractableResponse<Response> 요청_DELETE(String uri) {
        return given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    /**
     * 응답 상태 코드 검증
     * @param response API 응답
     * @param expectedStatusCode 기대하는 상태 코드
     */
    public static void 응답_상태코드_검증(ExtractableResponse<Response> response, int expectedStatusCode) {
        assertThat(response.statusCode()).isEqualTo(expectedStatusCode);
    }

    /**
     * Given-When-Then 패턴의 테스트 단계 실행
     *
     * @param given 주어진 상황
     * @param when 실행할 작업
     * @param then 검증할 결과
     * @param <T> 주어진 상황의 타입
     * @param <R> 실행 결과의 타입
     */
    public static <T, R> void 실행(
            Supplier<T> given,
            Function<T, R> when,
            java.util.function.Consumer<R> then) {
        // Given
        T givenResult = given.get();

        // When
        R whenResult = when.apply(givenResult);

        // Then
        then.accept(whenResult);
    }
} 