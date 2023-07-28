package com.project.Exceptions.CustomException.ValidationInput;

public class NoJwtAuthenticationException extends RuntimeException {
    public NoJwtAuthenticationException(String message) {
        super(message);}
}