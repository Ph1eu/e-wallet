package com.project.Exceptions.Handler;

import com.project.Payload.Response.ResponseEntityWrapper;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PessimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DatabaseExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(BusinessLogicExceptionsHandler.class);

    @ExceptionHandler(PessimisticLockException.class)
    public ResponseEntity<?> handlerPessimisticLockFailure(PessimisticLockException e){
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>("Internal Server Error");
        logger.error("PessimisticLockException occurred: "+e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntityWrapper);
    }
}
