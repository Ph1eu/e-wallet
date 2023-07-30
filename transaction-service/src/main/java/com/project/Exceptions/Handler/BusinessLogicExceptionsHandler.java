package com.project.Exceptions.Handler;

import com.project.Exceptions.CustomExceptions.BusinessLogic.InsufficientBalanceException;
import com.project.Exceptions.CustomExceptions.BusinessLogic.TransferFailedException;
import com.project.Exceptions.CustomExceptions.BusinessLogic.UserNotFoundException;
import com.project.Payload.Response.ResponseEntityWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BusinessLogicExceptionsHandler {
    private final Logger logger = LoggerFactory.getLogger(BusinessLogicExceptionsHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseEntityWrapper<?>> handleUserNotFoundException(UserNotFoundException ex) {
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>("USER NOT FOUND");
        logger.error("UserNotFoundException occurred: "+ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseEntityWrapper);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ResponseEntityWrapper<?>> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>("INSUFFICIENT BALANCE");
        logger.error("InsufficientBalanceException occurred: "+ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(responseEntityWrapper);
    }

    @ExceptionHandler(TransferFailedException.class)
    public ResponseEntity<ResponseEntityWrapper<?>> handleTransferFailedException(TransferFailedException ex) {
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>("TRANSFER FAILED DUE TO SERVER ERROR");
        logger.error("TransferFailedException occurred: "+ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntityWrapper);
    }
}
