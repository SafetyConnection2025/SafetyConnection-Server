package org.example.safetyconnection.common.exception;

public record ErrorResDTO(
	org.springframework.http.HttpStatus status,
	String message
) {
}
