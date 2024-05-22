package org.issk.controller;


import org.issk.exceptions.InvalidSessionException;
import org.issk.exceptions.SessionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSessionException.class)
    public ResponseEntity<String> handleInvalidSessionException(InvalidSessionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<String> handleSessionNotFoundException(SessionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
