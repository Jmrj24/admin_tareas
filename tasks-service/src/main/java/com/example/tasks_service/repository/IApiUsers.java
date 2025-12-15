package com.example.tasks_service.repository;

import com.example.tasks_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "users-service")
public interface IApiUsers {
    @GetMapping("/user/get/{idUser}")
    public Optional<UserDTO> getUserById(@PathVariable Long idUser);
}
