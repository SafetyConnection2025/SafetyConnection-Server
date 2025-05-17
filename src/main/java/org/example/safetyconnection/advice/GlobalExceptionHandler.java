package org.example.safetyconnection.advice;

import org.example.safetyconnection.exception.CompanionNotFoundException;
import org.example.safetyconnection.exception.LocationSaveFailedException;
import org.example.safetyconnection.exception.UserNameNotFoundException;
import org.springframework.http.HttpStatus;
import org.example.safetyconnection.dto.response.ErrorResDTO;
import org.example.safetyconnection.exception.UserIdNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler({UserIdNotFoundException.class, UserNameNotFoundException.class, CompanionNotFoundException.class})
	public ResponseEntity<ErrorResDTO> handleUserNotFoundException(Exception e) {
		ErrorResDTO errorResDTO = new ErrorResDTO(HttpStatus.NOT_FOUND, e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResDTO);
	}

	@ExceptionHandler(LocationSaveFailedException.class)
	public ResponseEntity<ErrorResDTO> handleLocationSaveFailed(Exception e) {
		ErrorResDTO errorResDTO = new ErrorResDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResDTO);
	}


	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResDTO> handleException(Exception e){
		ErrorResDTO errorResDTO = new ErrorResDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResDTO);
	}
}
