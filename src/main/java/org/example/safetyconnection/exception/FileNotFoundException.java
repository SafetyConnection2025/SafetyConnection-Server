package org.example.safetyconnection.exception;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String filename) {
        super("File not found: " + filename);
    }
}
