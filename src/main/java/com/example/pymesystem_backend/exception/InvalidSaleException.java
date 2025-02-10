package com.example.pymesystem_backend.exception;

public class InvalidSaleException extends RuntimeException {
    public InvalidSaleException(String message) {
        super(message);
    }
}
