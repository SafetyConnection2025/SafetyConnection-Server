package org.example.safetyconnection.jwt;

import lombok.Builder;

@Builder
public record JwtToken(Long userid, String accessToken, String refreshToken) {}