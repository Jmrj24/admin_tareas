package com.example.users_service.exception;


import com.example.users_service.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundExceptionUser.class)
    public ResponseEntity<ErrorResponse> notFound(NotFoundExceptionUser ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), String.valueOf(HttpStatus.NOT_FOUND)));
    }

    @ExceptionHandler(ConflictExceptionUser.class)
    public ResponseEntity<ErrorResponse> conflict(ConflictExceptionUser ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getMessage(), String.valueOf(HttpStatus.CONFLICT)));
    }
}
