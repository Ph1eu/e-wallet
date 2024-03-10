package com.project.exceptions.custom_exception.ValidationInput;

public class InvalidJwtException extends RuntimeException {
    public InvalidJwtException(String message) {
        super(message);
    }
}
