package com.example.pymesystem_backend.exception;

public class InvalidBillException extends RuntimeException {
    public InvalidBillException(String message) {
        super(message);
    }
}
