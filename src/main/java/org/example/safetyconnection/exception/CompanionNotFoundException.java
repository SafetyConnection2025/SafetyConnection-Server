package org.example.safetyconnection.exception;

public class CompanionNotFoundException extends RuntimeException{
	public CompanionNotFoundException(long companionId) {
		super("Companion not found: " + companionId);
	}
}
