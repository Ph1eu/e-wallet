package com.project.exceptions.custom_exception.ValidationInput;

public class NoJwtAuthenticationException extends RuntimeException {
    public NoJwtAuthenticationException(String message) {
        super(message);}
}