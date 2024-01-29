package com.project.exceptions.custom_exception.BusinessLogic;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}