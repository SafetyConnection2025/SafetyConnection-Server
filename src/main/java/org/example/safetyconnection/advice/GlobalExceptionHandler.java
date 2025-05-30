package org.example.safetyconnection.advice;

import org.example.safetyconnection.car.exception.CarIdAlreadyExistsException;
import org.example.safetyconnection.exception.*;
import org.springframework.http.HttpStatus;
import org.example.safetyconnection.dto.response.ErrorResDTO;
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

    @ExceptionHandler(CarIdAlreadyExistsException.class)
    public ResponseEntity<ErrorResDTO> AlreadyExists(CarIdAlreadyExistsException ex) {
        ErrorResDTO errorResDTO = new ErrorResDTO(HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResDTO);
    }

    @ExceptionHandler(LocationSaveFailedException.class)
    public ResponseEntity<ErrorResDTO> handleLocationSaveFailed(Exception e) {
        ErrorResDTO errorResDTO = new ErrorResDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResDTO);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResDTO> handleFileNotFound(FileNotFoundException e) {
        ErrorResDTO errorResDTO = new ErrorResDTO(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResDTO);
    }

    @ExceptionHandler(ObjectDetectionFailedException.class)
    public ResponseEntity<ErrorResDTO> handleObjectDetectionFailed(ObjectDetectionFailedException e) {
        ErrorResDTO errorResDTO = new ErrorResDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResDTO> handleException(Exception e) {
        ErrorResDTO errorResDTO = new ErrorResDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResDTO);
    }


}