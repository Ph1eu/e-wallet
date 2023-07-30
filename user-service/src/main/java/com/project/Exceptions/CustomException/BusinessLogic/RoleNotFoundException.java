package com.project.Exceptions.CustomException.BusinessLogic;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}