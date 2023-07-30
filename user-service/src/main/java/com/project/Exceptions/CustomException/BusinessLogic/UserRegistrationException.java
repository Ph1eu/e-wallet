package com.project.Exceptions.CustomException.BusinessLogic;

public class UserRegistrationException extends RuntimeException {
    public UserRegistrationException(String message) {
        super(message);
    }
}