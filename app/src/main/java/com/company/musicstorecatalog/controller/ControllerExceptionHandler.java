package com.company.musicstorecatalog.controller;

import com.company.musicstorecatalog.errors.CustomErrorResponse;
import com.company.musicstorecatalog.errors.exception.ArgumentIsNotANumberException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

//Code from Class on October 5 2022 Regarding custom handlers
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<CustomErrorResponse> handleOutOfRangeException(IllegalArgumentException ex) {
        CustomErrorResponse error = new CustomErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());

        ResponseEntity<CustomErrorResponse> returnVal = new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
        return returnVal;
    }
    @ExceptionHandler(value = {ArgumentIsNotANumberException.class})
    public ResponseEntity<CustomErrorResponse> handleNotANumberException(ArgumentIsNotANumberException ex) {

        CustomErrorResponse error = new CustomErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());

        ResponseEntity<CustomErrorResponse> returnVal = new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
        return returnVal;
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<CustomErrorResponse> handleNotANumberHttpRequestException(HttpMessageNotReadableException ex) {

        CustomErrorResponse error = new CustomErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());

        ResponseEntity<CustomErrorResponse> returnVal = new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
        return returnVal;
    }
    @ExceptionHandler(value = {ArithmeticException.class})
    public ResponseEntity<CustomErrorResponse> handleNotANumberHttpRequestException(ArithmeticException ex) {

        CustomErrorResponse error = new CustomErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());

        ResponseEntity<CustomErrorResponse> returnVal = new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
        return returnVal;
    }
    @ExceptionHandler(value = {EmptyResultDataAccessException.class})
    public ResponseEntity<CustomErrorResponse> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        String message = "No Album found";

        CustomErrorResponse error = new CustomErrorResponse(HttpStatus.NOT_FOUND, message);

        ResponseEntity<CustomErrorResponse> returnVal = new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        return returnVal;
    }
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<CustomErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        CustomErrorResponse error = new CustomErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getFieldError().getDefaultMessage());
        ResponseEntity<CustomErrorResponse> returnVal = new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
        return returnVal;
    }
    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<CustomErrorResponse> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        CustomErrorResponse error = new CustomErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        ResponseEntity<CustomErrorResponse> returnVal = new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
        return returnVal;
    }
}