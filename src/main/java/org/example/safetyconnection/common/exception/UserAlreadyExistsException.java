package org.example.safetyconnection.common.exception;

public class UserAlreadyExistsException extends RuntimeException{
	public UserAlreadyExistsException(Long id) {
		super("User " + id + " already exists");
	}
}
