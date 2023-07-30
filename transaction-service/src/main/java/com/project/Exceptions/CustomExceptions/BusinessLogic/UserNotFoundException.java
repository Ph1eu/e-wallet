package com.project.Exceptions.CustomExceptions.BusinessLogic;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
