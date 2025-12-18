package com.example.tasks_service.exception;

public class ServiceUnavailableExceptionTask extends RuntimeException {
    public ServiceUnavailableExceptionTask(String message) {
        super(message);
    }
}
