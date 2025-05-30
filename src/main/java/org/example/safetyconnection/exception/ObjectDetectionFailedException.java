package org.example.safetyconnection.exception;

public class ObjectDetectionFailedException extends RuntimeException {
    public ObjectDetectionFailedException() {
        super("Object detection failed");
    }
}
