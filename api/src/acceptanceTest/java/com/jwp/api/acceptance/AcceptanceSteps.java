package com.jwp.api.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 인수 테스트 단계(Steps) 도우미 클래스
 * 인수 테스트에서 공통으로 사용되는 HTTP 요청 및 응답 처리 메서드를 제공합니다.
 */
public class AcceptanceSteps {

    /**
     * HTTP GET 요청 수행
     *
     * @param url 요청 URL
     * @return API 응답
     */
    public static ExtractableResponse<Response> 요청_GET(String url) {
        return given().log().all()
                .when()
                .get(url)
                .then().log().all()
                .extract();
    }

    /**
     * HTTP GET 요청 수행 (쿼리 파라미터 포함)
     *
     * @param url 요청 URL
     * @param queryParams 쿼리 파라미터
     * @return API 응답
     */
    public static ExtractableResponse<Response> 요청_GET(String url, Map<String, Object> queryParams) {
        return given().log().all()
                .queryParams(queryParams)
                .when()
                .get(url)
                .then().log().all()
                .extract();
    }

    /**
     * HTTP POST 요청 수행
     *
     * @param url 요청 URL
     * @param body 요청 본문
     * @return API 응답
     */
    public static ExtractableResponse<Response> 요청_POST(String url, Object body) {
        return given().log().all()
                .contentType("application/json")
                .body(body)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }

    /**
     * HTTP PUT 요청 수행
     *
     * @param url 요청 URL
     * @param body 요청 본문
     * @return API 응답
     */
    public static ExtractableResponse<Response> 요청_PUT(String url, Object body) {
        return given().log().all()
                .contentType("application/json")
                .body(body)
                .when()
                .put(url)
                .then().log().all()
                .extract();
    }

    /**
     * HTTP DELETE 요청 수행
     *
     * @param url 요청 URL
     * @return API 응답
     */
    public static ExtractableResponse<Response> 요청_DELETE(String url) {
        return given().log().all()
                .when()
                .delete(url)
                .then().log().all()
                .extract();
    }

    /**
     * 응답 상태코드 검증
     *
     * @param response API 응답
     * @param expectedStatus 기대하는 상태코드
     */
    public static void 응답_상태코드_검증(ExtractableResponse<Response> response, int expectedStatus) {
        assertThat(response.statusCode()).isEqualTo(expectedStatus);
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
