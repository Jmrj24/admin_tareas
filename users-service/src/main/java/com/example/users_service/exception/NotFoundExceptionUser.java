package com.example.users_service.exception;

public class NotFoundExceptionUser extends RuntimeException {
    public NotFoundExceptionUser(String message) {
        super(message);
    }
}
