package com.project.api.common.error.database;

public class ExistedInformationException extends RuntimeException {
    public ExistedInformationException(String message) {
        super(message);
    }
}
