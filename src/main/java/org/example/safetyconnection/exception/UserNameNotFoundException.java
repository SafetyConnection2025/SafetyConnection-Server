package org.example.safetyconnection.exception;

public class UserNameNotFoundException extends RuntimeException {
	public UserNameNotFoundException(String name) {
		super("User Name Not Found: " + name);
	}
}
