package com.example.tasks_service.dto;

import java.time.LocalDate;

public record UserDTO (Long id, String name, String mail, LocalDate dateCreation) {}
