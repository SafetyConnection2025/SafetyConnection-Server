package org.example.safetyconnection.common.exception;

public class UserNameNotFoundException extends RuntimeException {
	public UserNameNotFoundException(String name) {
		super("User Name Not Found: " + name);
	}
}
