package com.project.exceptions.custom_exceptions.BusinessLogic;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
