package com.example.tasks_service.exception;

public class NotFoundExceptionTask extends RuntimeException {
    public NotFoundExceptionTask(String message) {
        super(message);
    }
}
