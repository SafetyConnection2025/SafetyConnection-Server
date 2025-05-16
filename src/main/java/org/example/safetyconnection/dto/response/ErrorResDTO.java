package org.example.safetyconnection.dto.response;

public record ErrorResDTO(
	org.springframework.http.HttpStatus status,
	String message
) {
}
