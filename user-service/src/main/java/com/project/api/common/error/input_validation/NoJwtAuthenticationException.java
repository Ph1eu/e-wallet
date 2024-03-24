package com.project.api.common.error.input_validation;

public class NoJwtAuthenticationException extends RuntimeException {
    public NoJwtAuthenticationException(String message) {
        super(message);
    }
}