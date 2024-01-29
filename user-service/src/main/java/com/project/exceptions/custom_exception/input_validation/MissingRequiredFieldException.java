package com.project.exceptions.custom_exception.ValidationInput;

public class MissingRequiredFieldException extends RuntimeException{
    public MissingRequiredFieldException(String message) {
        super(message);
    }
}
