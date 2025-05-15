package org.example.safetyconnection.jwt;

import lombok.Builder;

@Builder
public record JwtToken(Long userid, String username, String name, String accessToken, String refreshToken) {}