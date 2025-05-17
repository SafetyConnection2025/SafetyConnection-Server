package org.example.safetyconnection.exception;

public class UserIdNotFoundException extends RuntimeException {
	public UserIdNotFoundException(long userId) {
		super("UserId Not Found: " + userId);
	}
}
