package com.project.Exceptions.CustomException.ValidationInput;

public class InvalidDateFormatException extends RuntimeException {
    public InvalidDateFormatException(String field, String format) {
        super("Invalid date format for field '" + field + "'. Expected format: " + format);
    }
}