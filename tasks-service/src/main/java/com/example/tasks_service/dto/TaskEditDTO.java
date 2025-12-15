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
    @NotBlank
    private String title;
    private String description;
    @NotNull
    private LocalDateTime dateExpiration;
    @NotNull
    private TaskStatus status;
    @NotNull
    private TaskPriority priority;
    @NotNull
    private boolean notifications;
}
