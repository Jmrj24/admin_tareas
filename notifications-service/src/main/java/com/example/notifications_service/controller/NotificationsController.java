package com.example.notifications_service.controller;

import com.example.notifications_service.model.Notifications;
import com.example.notifications_service.service.INotificationsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {
    @Autowired
    private INotificationsService notificationsServ;

    @PostMapping("/create")
    public ResponseEntity<Void> sendNotification(@Valid @RequestBody Notifications notifications) {
        notificationsServ.sendNotification(notifications);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
