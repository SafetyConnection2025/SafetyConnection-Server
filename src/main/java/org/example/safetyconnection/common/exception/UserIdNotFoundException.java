package org.example.safetyconnection.common.exception;

public class UserIdNotFoundException extends RuntimeException {
	public UserIdNotFoundException(long userId) {
		super("UserId Not Found: " + userId);
	}
}
