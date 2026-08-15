package com.bookseva.common.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles Bean Validation failures (@NotBlank, @Min, etc.) on request DTOs.
     * Returns all violated fields in one shot so the user can fix everything at once.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("stage", "VALIDATION_FAILED");
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles image validation errors (e.g., zero photos, too many photos).
     */
    @ExceptionHandler(ImageValidationException.class)
    public ResponseEntity<Map<String, Object>> handleImageValidationException(ImageValidationException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("stage", "VALIDATION_FAILED");
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxSizeException(org.springframework.web.multipart.MaxUploadSizeExceededException exc) {
        Map<String, String> errors = new HashMap<>();
        errors.put("images", "One or more files exceed the maximum allowed size (5MB per file, 25MB total).");

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("stage", "VALIDATION_FAILED");
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles Cloudinary upload failures.
     */
    @ExceptionHandler(ImageUploadFailedException.class)
    public ResponseEntity<Map<String, Object>> handleImageUploadFailedException(ImageUploadFailedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("stage", "IMAGE_UPLOAD_FAILED");
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Handles DB constraint violations.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("stage", "LISTING_SAVE_FAILED");
        response.put("message", "Failed to save your listing. Please try again.");

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Handles generic JPA access exceptions.
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("stage", "LISTING_SAVE_FAILED");
        response.put("message", "Failed to save your listing. Please try again.");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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

    /**
     * Spring Security access denied (403) — e.g. @PreAuthorize fails.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("code", "FORBIDDEN");
        errorDetails.put("message", "You do not have permission to perform this action.");

        response.put("error", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Catch-all for uncategorised RuntimeExceptions.
     * Returns 400 by default; specific sub-cases are handled above.
     */
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
