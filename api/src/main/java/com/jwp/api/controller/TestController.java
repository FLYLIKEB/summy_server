package com.jwp.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "테스트 API", description = "테스트용 API")
public class TestController {

    @GetMapping("/hello")
    @Operation(summary = "헬로 월드", description = "간단한 테스트 API")
    public String hello() {
        return "Hello, World!";
    }
} 