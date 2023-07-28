package com.project.Exceptions.CustomException.ValidationInput;

public class InvalidJwtException extends RuntimeException{
    public InvalidJwtException(String message) {
        super(message);
    }
}
