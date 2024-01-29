package com.project.exceptions.custom_exceptions.BusinessLogic;

public class BalanceNotFoundException extends RuntimeException {
    public BalanceNotFoundException(String message) {
        super(message);
    }
}