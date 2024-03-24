package com.project.api.common.error.input_validation;

public class MissingRequiredFieldException extends RuntimeException {
    public MissingRequiredFieldException(String message) {
        super(message);
    }
}
