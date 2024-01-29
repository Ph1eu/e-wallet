package com.project.exceptions.custom_exception.Database;

public class ExistedInformationException extends RuntimeException{
    public ExistedInformationException(String message) {
        super(message);
    }
}
