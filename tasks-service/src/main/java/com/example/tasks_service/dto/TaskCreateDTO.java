package com.example.tasks_service.dto;

import com.example.tasks_service.model.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TaskCreateDTO (@NotBlank String title, String description, @NotNull LocalDateTime dateExpiration,
                             @NotNull TaskPriority priority, @NotNull boolean notifications, @NotNull Long idUser) {}
