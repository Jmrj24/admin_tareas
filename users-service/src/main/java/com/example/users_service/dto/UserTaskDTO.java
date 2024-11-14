package com.example.users_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTaskDTO {
    private String name;
    private String mail;
    private List<TaskDTO> listTask;
}
