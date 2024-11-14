package com.example.users_service.repository;

import com.example.users_service.dto.TaskDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "tasks-service")
public interface IApiTask {
    @GetMapping("/task/get/{idUser}")
    public List<TaskDTO> getAllTasksByUser(@PathVariable Long idUser);
}
