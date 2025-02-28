package com.postiy.postify.excetion;

import com.postiy.postify.dto.postdto.validationDto.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CostomValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorResponse>> handelValidationException(MethodArgumentNotValidException ex){
        List<ValidationErrorResponse> errors=new ArrayList<>();
       for (FieldError error : ex.getBindingResult().getFieldErrors()
       ){
           errors.add(new ValidationErrorResponse(error.getField(),error.getDefaultMessage()));

        }
       return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
