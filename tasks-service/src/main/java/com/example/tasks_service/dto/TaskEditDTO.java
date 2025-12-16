package com.example.tasks_service.dto;

import com.example.tasks_service.model.TaskPriority;
import com.example.tasks_service.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class TaskEditDTO {
    private String title;
    private String description;
    private LocalDateTime dateExpiration;
    private TaskStatus status;
    private TaskPriority priority;
    private Boolean notifications;
}
