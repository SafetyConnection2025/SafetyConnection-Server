package org.example.safetyconnection.exception;

public class UserAlreadyExistsException extends RuntimeException{
	public UserAlreadyExistsException(Long id) {
		super("User " + id + " already exists");
	}
}
