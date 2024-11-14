package com.example.notifications_service.service;

import com.example.notifications_service.model.Notifications;

public interface INotificationsService {
    public void sendNotification(Notifications notifications);
}
