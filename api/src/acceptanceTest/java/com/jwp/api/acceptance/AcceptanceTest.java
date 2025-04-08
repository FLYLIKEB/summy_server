package com.jwp.api.acceptance;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 인수 테스트를 위한 기본 클래스
 * 모든 인수 테스트는 이 클래스를 상속받아 구현합니다.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {

    /**
     * 랜덤으로 할당된 서버 포트
     */
    @LocalServerPort
    private int port;

    /**
     * API 요청 명세
     */
    protected RequestSpecification spec;

    /**
     * 테스트 실행 전 설정 초기화
     */
    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        this.spec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(port)
                .addFilter((requestSpec, responseSpec, ctx) -> {
                    return ctx.next(requestSpec, responseSpec);
                })
                .build();

        // 테스트 전 데이터 초기화
        initializeTestData();
    }

    /**
     * 테스트용 데이터 초기화
     * 상속받은 클래스에서 필요한 테스트 데이터를 초기화합니다.
     */
    protected abstract void initializeTestData();

    /**
     * API 경로 생성
     * @param path 상대 경로
     * @return 전체 API 경로
     */
    protected String getUrl(String path) {
        return "http://localhost:" + port + path;
    }
}
