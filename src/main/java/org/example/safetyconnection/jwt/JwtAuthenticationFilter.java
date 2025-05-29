package org.example.safetyconnection.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 내가 추가한 부분
        String bearerToken = request.getHeader("access");
        log.info(">>> Authorization 헤더 값: {}", bearerToken);

        String token = null;

        if (bearerToken != null && bearerToken.toLowerCase().startsWith("bearer ")) {
            token = bearerToken.substring(7).trim();
        }

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        /* 기존 코드 부분
        String token = resolveToken(request);

        log.info("token = {}", token);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
         */

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        return request.getHeader("access");
    }
}
