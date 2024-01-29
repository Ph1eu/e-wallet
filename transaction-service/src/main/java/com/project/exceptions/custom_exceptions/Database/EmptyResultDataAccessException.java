package com.project.exceptions.custom_exceptions.Database;

public class EmptyResultDataAccessException extends DatabaseException {

    public EmptyResultDataAccessException(String message) {
        super(message);
    }

    public EmptyResultDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}