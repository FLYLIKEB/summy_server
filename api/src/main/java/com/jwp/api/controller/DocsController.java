package com.jwp.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/docs")
public class DocsController {

    @GetMapping
    @ResponseBody
    public String apiDocs() {
        return "<html><head><title>API 문서</title></head><body>"
                + "<h1>API 문서</h1>"
                + "<h2>테스트 API</h2>"
                + "<ul>"
                + "<li><b>GET /api/test/hello</b> - 간단한 테스트 API</li>"
                + "</ul>"
                + "<h2>xAI API</h2>"
                + "<ul>"
                + "<li><b>POST /api/xai/complete</b> - 사용자 메시지를 받아 xAI API에 요청하고 응답을 반환합니다.</li>"
                + "</ul>"
                + "<hr>"
                + "<p>요청 예시:</p>"
                + "<pre>"
                + "curl -X POST http://localhost:8080/api/xai/complete \\\n"
                + "  -H \"Content-Type: application/json\" \\\n"
                + "  -d '{\"message\": \"Testing. Just say hi and hello world and nothing else.\"}'"
                + "</pre>"
                + "</body></html>";
    }
} 