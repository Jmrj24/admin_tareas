package com.example.tasks_service.dto;

import com.example.tasks_service.model.TaskPriority;
import com.example.tasks_service.model.TaskStatus;
import java.time.LocalDateTime;

public record TaskResponseDTO(Long id, String title, String description, LocalDateTime dateCreation, LocalDateTime dateExpiration,
        TaskStatus status, TaskPriority priority, boolean notifications, Long idUser) {
}