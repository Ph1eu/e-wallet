package com.project.Exceptions.CustomException.Database;

public class ExistedInformationException extends RuntimeException{
    public ExistedInformationException(String message) {
        super(message);
    }
}
