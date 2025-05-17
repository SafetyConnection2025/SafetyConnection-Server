package org.example.safetyconnection.exception;

public class LocationSaveFailedException extends RuntimeException {
	public LocationSaveFailedException(long userID) {
		super("Location save failed. User ID: " + userID);
	}
}
