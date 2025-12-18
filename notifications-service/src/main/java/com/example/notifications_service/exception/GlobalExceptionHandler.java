package com.example.notifications_service.exception;

import com.example.notifications_service.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundExceptionNotifications.class)
    public ResponseEntity<ErrorResponse> notFound(NotFoundExceptionNotifications ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), String.valueOf(HttpStatus.NOT_FOUND)));
    }

    @ExceptionHandler(FailedExceptionNotifications.class)
    public ResponseEntity<ErrorResponse> badRequest(FailedExceptionNotifications ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorResponse(ex.getMessage(), String.valueOf(HttpStatus.BAD_REQUEST)));
    }

    @ExceptionHandler(ServiceUnavailableExceptionNotifications.class)
    public ResponseEntity<ErrorResponse> badRequest(ServiceUnavailableExceptionNotifications ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorResponse(ex.getMessage(), String.valueOf(HttpStatus.SERVICE_UNAVAILABLE)));
    }
}
