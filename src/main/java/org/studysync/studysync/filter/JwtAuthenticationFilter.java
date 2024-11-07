package org.studysync.studysync.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.studysync.studysync.config.HttpErrorCode;
import org.studysync.studysync.constant.TokenType;
import org.studysync.studysync.exception.HttpErrorException;
import org.studysync.studysync.exception.ErrorResponseDto;
import org.studysync.studysync.provider.JwtTokenProvider;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter는 클라이언트 요청 시 JWT 인증을 하기위해 설치하는 커스텀 필터로, UsernamePasswordAuthenticationFilter 이전에 실행됨
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {
        if (request.getRequestURI().equals("/api/auth/login") && request.getMethod().equals(HttpMethod.POST.name())) {
            chain.doFilter(request, response);
            return;
        }
        if (request.getRequestURI().equals("/api/auth/refreshToken") && request.getMethod().equals(HttpMethod.POST.name())) {
            chain.doFilter(request, response);
            return;
        }

        if (request.getRequestURI().equals("/api/auth/logout") && request.getMethod().equals(HttpMethod.POST.name())) {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = null;
        try {
            accessToken = jwtTokenProvider.resolveToken(request.getHeader("Authorization"));
        } catch (HttpErrorException e) {
            chain.doFilter(request, response);
            return;
        }

        try {
            jwtTokenProvider.validateToken(TokenType.ACCESS_TOKEN, accessToken);
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (Exception e) {
            jwtExceptionHandler(response, e);
        }
    }


    // 토큰에 대한 오류가 발생했을 때, 커스터마이징해서 Exception 처리 값을 클라이언트에게 알려준다.
    public void jwtExceptionHandler(HttpServletResponse response, Exception e) {
        HttpErrorCode httpErrorCode = HttpErrorCode.InternalServerError;
        if (e instanceof HttpErrorException) {
            httpErrorCode = ((HttpErrorException) e).getHttpErrorCode();
        }

        response.setStatus(httpErrorCode.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(ErrorResponseDto.from(httpErrorCode));
            response.getWriter().write(json);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
