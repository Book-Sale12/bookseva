package com.bookseva.common.exception;
import com.bookseva.message.entity.Message;


public class UnverifiedAccountException extends RuntimeException {
    public UnverifiedAccountException(String message) {
        super(message);
    }
}
