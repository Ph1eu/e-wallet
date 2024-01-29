package com.project.exceptions.custom_exceptions.ValidationInput;

public class InvalidPhoneNumberFormatException extends RuntimeException {
    public InvalidPhoneNumberFormatException() {
        super("Invalid phone number format.");
    }
}