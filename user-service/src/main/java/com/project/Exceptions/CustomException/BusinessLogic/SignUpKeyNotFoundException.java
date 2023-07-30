package com.project.Exceptions.CustomException.BusinessLogic;

public class SignUpKeyNotFoundException extends  RuntimeException{
    public SignUpKeyNotFoundException(String message) {
        super(message);
    }
}
