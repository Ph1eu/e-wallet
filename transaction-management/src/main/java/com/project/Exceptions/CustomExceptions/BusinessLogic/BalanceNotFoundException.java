package com.project.Exceptions.CustomExceptions.BusinessLogic;

public class BalanceNotFoundException extends RuntimeException {
    public BalanceNotFoundException(String message) {
        super(message);
    }
}