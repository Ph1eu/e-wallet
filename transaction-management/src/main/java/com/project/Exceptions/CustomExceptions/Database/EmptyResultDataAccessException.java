package com.project.Exceptions.CustomExceptions.Database;

public class EmptyResultDataAccessException extends DatabaseException {

    public EmptyResultDataAccessException(String message) {
        super(message);
    }

    public EmptyResultDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}