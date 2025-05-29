package org.example.safetyconnection.dto.request;

public record CompanionCallRequestDTO(
	Long companionId,
	Double latitude,
	Double longitude,
	FCMNotificationRequestDTO.MessageDTO message
) {}
