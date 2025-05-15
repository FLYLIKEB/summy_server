package com.jwp.api.config;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * CORS 필터
 * 모든 HTTP 요청에 대해 CORS 헤더를 추가하는 필터입니다.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    /**
     * 필터 초기화
     * @param filterConfig 필터 설정
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 초기화 코드가 필요하지 않음
    }

    /**
     * 필터 처리
     * 모든 HTTP 응답에 CORS 헤더를 추가합니다.
     * 
     * @param req 서블릿 요청
     * @param res 서블릿 응답
     * @param chain 필터 체인
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        
        // 요청 오리진 가져오기
        String origin = request.getHeader("Origin");
        
        // 허용된 오리진인지 확인
        if (origin != null && (origin.equals("http://localhost:3000") || 
                              origin.equals("http://52.78.150.124:8080") || 
                              origin.equals("https://summy-sigma.vercel.app"))) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        } else {
            // 기본값으로 로컬 개발 서버 설정
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        }
        
        // 나머지 CORS 헤더 설정
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Content-Length, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
        // OPTIONS 요청(preflight 요청)에 대한 처리
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    /**
     * 필터 소멸
     */
    @Override
    public void destroy() {
        // 소멸 코드가 필요하지 않음
    }
} 