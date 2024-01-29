package com.project.exceptions.custom_exception.BusinessLogic;

public class SignUpKeyNotFoundException extends  RuntimeException{
    public SignUpKeyNotFoundException(String message) {
        super(message);
    }
}
