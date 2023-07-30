package com.project.Exceptions.CustomExceptions.BusinessLogic;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
