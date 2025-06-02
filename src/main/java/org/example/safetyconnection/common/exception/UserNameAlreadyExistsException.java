package org.example.safetyconnection.common.exception;

public class UserNameAlreadyExistsException extends RuntimeException {
	public UserNameAlreadyExistsException(String userName) {

      super("User Name Already Exists: " + userName);
	}
}
