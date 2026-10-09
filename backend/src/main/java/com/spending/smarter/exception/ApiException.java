package com.spending.smarter.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}