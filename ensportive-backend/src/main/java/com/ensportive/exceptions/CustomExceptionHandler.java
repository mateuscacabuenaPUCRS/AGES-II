package com.ensportive.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleEntityNotFoundException(EntityNotFoundException e, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO();

        errorDTO.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorDTO.setMessage(e.getMessage());
        errorDTO.setTimestamp(new Date(System.currentTimeMillis()));

        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO();

        errorDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorDTO.setMessage(e.getMessage());
        errorDTO.setTimestamp(new Date(System.currentTimeMillis()));

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO();

        errorDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorDTO.setMessage(e.getMessage());
        errorDTO.setTimestamp(new Date(System.currentTimeMillis()));

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}
