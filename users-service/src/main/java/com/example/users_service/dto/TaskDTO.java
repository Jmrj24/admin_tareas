package com.example.users_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dateCreation;
    private LocalDateTime dateExpiration;
    private String state;
    private String priority;
    private boolean notifications;
    private Long idUser;
}
