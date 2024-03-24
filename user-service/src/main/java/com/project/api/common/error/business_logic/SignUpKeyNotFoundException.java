package com.project.api.common.error.business_logic;

public class SignUpKeyNotFoundException extends RuntimeException {
    public SignUpKeyNotFoundException(String message) {
        super(message);
    }
}
