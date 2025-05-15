package com.jwp.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger(OpenAPI) 설정
 * API 문서화를 위한 Swagger 설정을 제공합니다.
 */
@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI 설정
     * @return OpenAPI 객체
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API 문서")
                        .version("v1.0")
                        .description("API 문서입니다"));
    }
} 