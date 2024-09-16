package com.clothes.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.clothes.exception.NotFoundExeception;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundExeception.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundExeception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
