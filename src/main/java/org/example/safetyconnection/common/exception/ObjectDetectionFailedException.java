package org.example.safetyconnection.common.exception;

public class ObjectDetectionFailedException extends RuntimeException {
    public ObjectDetectionFailedException() {
        super("Object detection failed");
    }
}
