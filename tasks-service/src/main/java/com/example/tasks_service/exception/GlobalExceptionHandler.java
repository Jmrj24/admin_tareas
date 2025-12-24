package com.example.tasks_service.exception;

import com.example.tasks_service.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundExceptionTask.class)
    public ResponseEntity<ErrorResponse> notFound(NotFoundExceptionTask ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), String.valueOf(HttpStatus.NOT_FOUND)));
    }

    @ExceptionHandler(ConflictExceptionTask.class)
    public ResponseEntity<ErrorResponse> conflict(ConflictExceptionTask ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getMessage(), String.valueOf(HttpStatus.CONFLICT)));
    }

    @ExceptionHandler(BadRequestExceptionTask.class)
    public ResponseEntity<ErrorResponse> badRequest(BadRequestExceptionTask ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage(), String.valueOf(HttpStatus.BAD_REQUEST)));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> EnumError(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("El valor enviado no es válido",  String.valueOf(HttpStatus.BAD_REQUEST)));
    }

    @ExceptionHandler(ServiceUnavailableExceptionTask.class)
    public ResponseEntity<ErrorResponse> ServiceUnavailable(ServiceUnavailableExceptionTask ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorResponse(ex.getMessage(),  String.valueOf(HttpStatus.SERVICE_UNAVAILABLE)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> MethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Dato incorrecto en campo "+ Objects.requireNonNull(ex.getFieldError()).getField(), String.valueOf(HttpStatus.BAD_REQUEST)));
    }
}
