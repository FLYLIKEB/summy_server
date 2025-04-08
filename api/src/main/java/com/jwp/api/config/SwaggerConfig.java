package com.jwp.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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
        // API 정보 설정
        Info info = new Info()
                .title("사용자 관리 API")
                .version("v1")
                .description("사용자 관리를 위한 RESTful API")
                .contact(new Contact()
                        .name("JWP")
                        .email("jwp@example.com")
                        .url("https://github.com/FLYLIKEB/summy_server"))
                .license(new License().name("MIT License"));

        // 서버 정보 설정
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local Server");

        // 보안 스키마 설정 (필요 시 주석 해제)
        /*
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
        */

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer))
                // 보안 스키마 설정 (필요 시 주석 해제)
                //.components(new Components().addSecuritySchemes("bearer-key", securityScheme));
                .components(new Components());
    }
} 