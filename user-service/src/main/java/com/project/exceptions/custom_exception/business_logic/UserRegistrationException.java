package com.project.exceptions.custom_exception.BusinessLogic;

public class UserRegistrationException extends RuntimeException {
    public UserRegistrationException(String message) {
        super(message);
    }
}