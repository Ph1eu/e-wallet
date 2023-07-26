package com.project.Exceptions;

public class UserRegistrationException extends RuntimeException {
    public UserRegistrationException(String message) {
        super(message);
    }
}