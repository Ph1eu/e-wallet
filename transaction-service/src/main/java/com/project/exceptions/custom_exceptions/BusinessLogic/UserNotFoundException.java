package com.project.exceptions.custom_exceptions.BusinessLogic;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
