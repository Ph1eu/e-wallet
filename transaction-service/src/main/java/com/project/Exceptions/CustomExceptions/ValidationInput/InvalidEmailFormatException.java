package com.project.Exceptions.CustomExceptions.ValidationInput;

public class InvalidEmailFormatException extends RuntimeException {
    public InvalidEmailFormatException() {
        super("Invalid email format.");
    }
}