package org.example.safetyconnection.common.exception;

public class CompanionNotFoundException extends RuntimeException{
	public CompanionNotFoundException(long companionId) {
		super("Companion not found: " + companionId);
	}
}
