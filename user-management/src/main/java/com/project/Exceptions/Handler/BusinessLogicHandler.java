package com.project.Exceptions.Handler;

import com.project.Exceptions.CustomException.BusinessLogic.RoleNotFoundException;
import com.project.Exceptions.CustomException.BusinessLogic.SignUpKeyNotFoundException;
import com.project.Payload.Response.ResponseEntityWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BusinessLogicHandler {
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<?> handleRoleNotFound(RoleNotFoundException e){
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseEntityWrapper);
    }
    @ExceptionHandler(SignUpKeyNotFoundException.class)
    public ResponseEntity<?> handleSignUpKeyNotFound(SignUpKeyNotFoundException e){
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseEntityWrapper);
    }
}
