package com.bookseva.common.exception;

public class ImageValidationException extends RuntimeException {
    private final String field;

    public ImageValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
