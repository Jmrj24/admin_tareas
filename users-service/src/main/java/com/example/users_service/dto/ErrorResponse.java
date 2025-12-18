package com.example.users_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class ErrorResponse {
    private final String message;
    private final String error;
}
