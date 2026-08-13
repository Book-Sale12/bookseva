package com.bookseva.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("code", "VALIDATION_ERROR");
        errorDetails.put("message", "Validation failed");
        errorDetails.put("details", errors);
        
        response.put("error", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnverifiedAccountException.class)
    public ResponseEntity<Map<String, Object>> handleUnverifiedAccountException(UnverifiedAccountException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("code", "UNVERIFIED_ACCOUNT");
        errorDetails.put("message", ex.getMessage());
        
        response.put("error", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("code", "CONFLICT");
        errorDetails.put("message", "This record already exists or violates constraints.");
        
        response.put("error", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeExceptions(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("code", "BAD_REQUEST");
        errorDetails.put("message", ex.getMessage());
        
        response.put("error", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
