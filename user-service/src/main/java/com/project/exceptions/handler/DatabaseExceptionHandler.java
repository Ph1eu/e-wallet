package com.project.exceptions.handler;

import com.project.exceptions.custom_exception.Database.ExistedInformationException;
import com.project.exceptions.custom_exception.Database.UserNotFoundException;
import com.project.payload.response.ResponseEntityWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DatabaseExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseExceptionHandler.class);

    @ExceptionHandler(ExistedInformationException.class)
    public ResponseEntity<?> handleExistedInformation(ExistedInformationException e) {
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>(e.getMessage());
        logger.error("ExistedInformationException occurred :" + e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseEntityWrapper);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException e) {
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>(e.getMessage());
        logger.error("UserNotFoundException occurred :" + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseEntityWrapper);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFound(UsernameNotFoundException e) {
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>(e.getMessage());
        logger.error("UsernameNotFoundException occurred :" + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseEntityWrapper);
    }
}
