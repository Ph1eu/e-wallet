package com.project.Exceptions;

public class TransferFailedException extends RuntimeException{
    public TransferFailedException(String message) {
        super(message);
    }
}
