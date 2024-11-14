package com.example.tasks_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationsDTO {
    private String subject;
    private String text;
    private Long idUser;
}
