package org.example.safetyconnection.common.exception;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String filename) {
        super("File not found: " + filename);
    }
}
