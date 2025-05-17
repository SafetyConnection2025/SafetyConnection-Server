package org.example.safetyconnection.car.exception;

public class CarIdAlreadyExistsException extends RuntimeException{
	public CarIdAlreadyExistsException(String carId) {
		super("Car ID already exists: " + carId);
	}
}
