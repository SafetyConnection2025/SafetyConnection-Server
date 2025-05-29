package org.example.safetyconnection.dto.request;

import java.time.LocalDateTime;

import org.example.safetyconnection.entity.Member;

public record CompanionReqDTO(long userId, long compId, double latitude, double longitude) {
}
