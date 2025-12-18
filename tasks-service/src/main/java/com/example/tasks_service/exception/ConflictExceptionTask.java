package com.example.tasks_service.exception;

public class ConflictExceptionTask extends RuntimeException {
    public ConflictExceptionTask(String message) {
        super(message);
    }
}
