package com.project.Exceptions.Handler;

import com.project.Exceptions.CustomExceptions.ValidationInput.*;
import com.project.Payload.Response.ResponseEntityWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationInputExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(BusinessLogicExceptionsHandler.class);
    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<?> handleInvalidDateFormatException(InvalidDateFormatException invalidDateFormatException){
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>("invalid date format");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityWrapper);
    }
    @ExceptionHandler(InvalidEmailFormatException.class)
    public ResponseEntity<?> handleInvalidEmailFormatException(InvalidEmailFormatException invalidEmailFormatException){
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>("invalid email format");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityWrapper);
    }
    @ExceptionHandler(InvalidPhoneNumberFormatException.class)
    public ResponseEntity<?> handleInvalidPhoneNumberFormatException(InvalidPhoneNumberFormatException exception){
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>("invalid email format");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityWrapper);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleRequestParamException(MissingServletRequestParameterException exception){
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityWrapper);
    }
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> handleNumberFormatFieldException(NumberFormatException exception){
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>("Wrong number Format");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityWrapper);
    }
}
