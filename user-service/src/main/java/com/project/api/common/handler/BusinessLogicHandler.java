package com.project.api.common.handler;

import com.project.api.common.error.business_logic.RoleNotFoundException;
import com.project.api.common.error.business_logic.SignUpKeyNotFoundException;
import com.project.api.common.model.ResponseEntityWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BusinessLogicHandler {
    private static final Logger logger = LoggerFactory.getLogger(BusinessLogicHandler.class);

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<?> handleRoleNotFound(RoleNotFoundException e) {
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>(e.getMessage());
        logger.error("RoleNotFoundException occurred :" + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseEntityWrapper);
    }

    @ExceptionHandler(SignUpKeyNotFoundException.class)
    public ResponseEntity<?> handleSignUpKeyNotFound(SignUpKeyNotFoundException e) {
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>(e.getMessage());
        logger.error("SignUpKeyNotFoundException occurred :" + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseEntityWrapper);
    }
}
