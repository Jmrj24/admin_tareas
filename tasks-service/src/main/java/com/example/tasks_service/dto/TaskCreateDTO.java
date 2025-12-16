package com.example.tasks_service.dto;

import com.example.tasks_service.model.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateDTO {
    @NotBlank
    private String title;
    private String description;
    @NotNull
    private LocalDateTime dateExpiration;
    @NotNull
    private TaskPriority priority;
    @NotNull
    private boolean notifications;
    @NotNull
    private Long idUser;
}
