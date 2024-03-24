package com.project.api.common.handler;

import com.project.api.common.error.input_validation.MissingRequiredFieldException;
import com.project.api.common.model.ResponseEntityWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationInputHandler {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseExceptionHandler.class);

    @ExceptionHandler(MissingRequiredFieldException.class)
    public ResponseEntity<?> handleMissingRequiredField(MissingRequiredFieldException e) {
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>(e.getMessage());
        logger.error("MissingRequiredFieldException occurred :" + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityWrapper);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleRequestParamException(MissingServletRequestParameterException exception) {
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>(exception.getMessage());
        logger.error("MissingServletRequestParameterException occurred :" + exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityWrapper);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<?> handlePathVariableException(MissingPathVariableException exception) {
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>(exception.getMessage());
        logger.error("MissingPathVariableException occurred :" + exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityWrapper);
    }

}
