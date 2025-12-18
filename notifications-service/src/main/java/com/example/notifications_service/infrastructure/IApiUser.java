package com.example.notifications_service.infrastructure;

import com.example.notifications_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-service")
public interface IApiUser {
    @GetMapping("/user/get/{idUser}")
    public UserDTO getUserById(@PathVariable Long idUser);
}
