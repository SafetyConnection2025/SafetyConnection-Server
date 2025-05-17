package org.example.safetyconnection.exception;

public class UserNameAlreadyExistsException extends RuntimeException {
	public UserNameAlreadyExistsException(String userName) {

      super("User Name Already Exists: " + userName);
	}
}
