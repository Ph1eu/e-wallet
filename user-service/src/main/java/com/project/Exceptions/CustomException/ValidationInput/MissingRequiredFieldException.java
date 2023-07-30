package com.project.Exceptions.CustomException.ValidationInput;

public class MissingRequiredFieldException extends RuntimeException{
    public MissingRequiredFieldException(String message) {
        super(message);
    }
}
