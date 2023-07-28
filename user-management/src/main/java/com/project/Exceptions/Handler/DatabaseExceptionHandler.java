package com.project.Exceptions.Handler;

import com.project.Exceptions.CustomException.Database.ExistedInformationException;
import com.project.Payload.Response.ResponseEntityWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DatabaseExceptionHandler{
    @ExceptionHandler(ExistedInformationException.class)
    public ResponseEntity<?> handleExistedInformation(ExistedInformationException e){
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityWrapper);
    }
}
