package com.project.Exceptions.CustomException.ValidationInput;

public class ExpiredJwtException extends RuntimeException{
    public ExpiredJwtException(String message) {
        super(message);
    }
}
