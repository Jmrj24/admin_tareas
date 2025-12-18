package com.example.tasks_service.exception;

public class BadRequestExceptionTask extends RuntimeException {
    public BadRequestExceptionTask(String message) {
        super(message);
    }
}
