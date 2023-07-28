package com.project.Exceptions.CustomExceptions.BusinessLogic;

public class TransferFailedException extends RuntimeException{
    public TransferFailedException(String message) {
        super(message);
    }
}
