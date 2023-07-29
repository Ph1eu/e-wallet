package com.project.Exceptions.Handler;

import com.project.Exceptions.CustomException.ValidationInput.MissingRequiredFieldException;
import com.project.Exceptions.CustomException.ValidationInput.NoJwtAuthenticationException;
import com.project.Payload.Response.ResponseEntityWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class ValidationInputHandler {
    @ExceptionHandler(NoJwtAuthenticationException.class)
    public ResponseEntity<?> handleJwtAuthenticationFailure(NoJwtAuthenticationException e){
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseEntityWrapper);
    }
    @ExceptionHandler(MissingRequiredFieldException.class)
    public ResponseEntity<?> handleMissingRequiredField(MissingRequiredFieldException e){
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityWrapper);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleRequestParamException(MissingServletRequestParameterException exception){
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityWrapper);
    }
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<?> handlePathVariableException(MissingPathVariableException exception){
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityWrapper);
    }

}
