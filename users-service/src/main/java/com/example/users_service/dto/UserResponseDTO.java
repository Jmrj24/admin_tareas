package com.example.users_service.dto;

import java.time.LocalDate;

public record UserResponseDTO(Long id, String name, String mail, LocalDate dateCreation) {
}
