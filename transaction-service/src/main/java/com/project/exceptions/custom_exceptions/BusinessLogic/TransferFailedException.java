package com.project.exceptions.custom_exceptions.BusinessLogic;

public class TransferFailedException extends RuntimeException {
    public TransferFailedException(String message) {
        super(message);
    }
}
