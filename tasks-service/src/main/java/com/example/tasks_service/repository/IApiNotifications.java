package com.example.tasks_service.repository;

import com.example.tasks_service.dto.NotificationsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notifications-service")
public interface IApiNotifications {
    @PostMapping("/notifications/create")
    public void sendNotification(@RequestBody NotificationsDTO notificationsDTO);
}
