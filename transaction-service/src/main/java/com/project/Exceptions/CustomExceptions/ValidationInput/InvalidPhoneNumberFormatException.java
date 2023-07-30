package com.project.Exceptions.CustomExceptions.ValidationInput;

public class InvalidPhoneNumberFormatException extends RuntimeException {
    public InvalidPhoneNumberFormatException() {
        super("Invalid phone number format.");
    }
}