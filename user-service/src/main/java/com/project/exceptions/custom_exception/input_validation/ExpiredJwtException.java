package com.project.exceptions.custom_exception.ValidationInput;

public class ExpiredJwtException extends RuntimeException {
    public ExpiredJwtException(String message) {
        super(message);
    }
}
