package com.example.users_service.exception;

public class ConflictExceptionUser extends RuntimeException {
    public ConflictExceptionUser(String message) {
        super(message);
    }
}
