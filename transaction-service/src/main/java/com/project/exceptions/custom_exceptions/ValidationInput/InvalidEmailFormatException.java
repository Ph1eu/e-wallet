package com.project.exceptions.custom_exceptions.ValidationInput;

public class InvalidEmailFormatException extends RuntimeException {
    public InvalidEmailFormatException() {
        super("Invalid email format.");
    }
}