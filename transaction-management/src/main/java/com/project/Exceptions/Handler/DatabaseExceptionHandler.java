package com.project.Exceptions.Handler;

import com.project.Payload.Response.ResponseEntityWrapper;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PessimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DatabaseExceptionHandler {
    @ExceptionHandler(PessimisticLockException.class)
    public ResponseEntity<?> handlerPessimisticLockFailure(PessimisticLockException e){
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>("Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntityWrapper);
    }
}
