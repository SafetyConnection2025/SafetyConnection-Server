package org.example.safetyconnection.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.entity.Member;
import org.example.safetyconnection.service.facade.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    // 사용자 권한 key
    public static final String AUTHORIZATION_KEY = "auth";

    private final SecretKey key;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, CustomUserDetailsService customUserDetailsService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.customUserDetailsService = customUserDetailsService;
    }

    public JwtToken generateToken(Authentication authentication, Member member) {
        // 권한 불러오기
        String authorities = member.getMemberType() != null ? member.getMemberType().name() : "";
        String username = member.getUsername();
        Long userId = member.getUserId();

        long now = (new Date()).getTime();

        // generate accessToken
        Date accessTokenExpirationTime = new Date(now + 1000 * 60 * 10);  // access token 유효 시간, 10분
        String accessToken = Jwts.builder()
                .subject(authentication.getName())
                .claim("userId", userId)
                .claim("username", username)
                .claim(AUTHORIZATION_KEY, authorities)
                .expiration(accessTokenExpirationTime)
                .signWith(key)
                .compact();

        // generate refreshToken
        Date refreshTokenExpirationTime = new Date(now + 1000 * 60 * 30);  // refresh token 유효 시간, 30분
        String refreshToken = Jwts.builder()
                .claim("userId", userId)
                .claim("username", username)
                .expiration(refreshTokenExpirationTime)
                .signWith(key)
                .compact();

        return JwtToken.builder()
                .userid(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        // token에 담긴 auth claim의 value 조사
        if (claims.get(AUTHORIZATION_KEY) == null) {
            throw new RuntimeException("권한 정보가 없습니다.");
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORIZATION_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        String username = claims.get("username").toString();
        UserDetails principal = customUserDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰의 유효성 검증 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("유효하지 않은 토큰입니다.", e);
        } catch (ExpiredJwtException e) {
            log.info("만료된 토큰입니다.", e);
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            log.info("지원하지 않는 형식의 토큰입니다.", e);
        } catch (Exception e) {
            log.info("예기치 못한 오류가 발생했습니다.", e);
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            // 토큰을 파싱하고 claim 정보를 반환
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.get("userId", Long.class);
    }
}