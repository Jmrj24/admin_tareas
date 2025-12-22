package com.example.tasks_service.infrastructure;

import com.example.tasks_service.dto.NotificationsDTO;
import com.example.tasks_service.exception.ServiceUnavailableExceptionTask;
import com.example.tasks_service.model.Task;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExternalApiService {
    @Autowired
    private IApiUsers apiUsers;

    @Autowired
    private IApiNotifications apiNotifications;

    @CircuitBreaker(name = "users-service", fallbackMethod = "fallbackMethodValidateUserById")
    @Retry(name = "users-service")
    public void ValidateUserById(Long idUser) {
        apiUsers.getUserById(idUser);
    }

    public void fallbackMethodValidateUserById(Long idUser, Throwable throwable) {
        throw new ServiceUnavailableExceptionTask("No se pudo verificar el usuario con id: " + idUser + ". Servicio de usuarios no disponible");
    }

    @CircuitBreaker(name = "notifications-service", fallbackMethod = "fallbackMethodSendNotifications")
    @Retry(name = "notifications-service")
    public void sendNotifications(Task task, String text) {
        apiNotifications.sendNotification(new NotificationsDTO(task.getTitle(), text , task.getIdUser()));
    }

    private void fallbackMethodSendNotifications(Task task, String text, Throwable throwable) {
        throw new ServiceUnavailableExceptionTask("No se pudo enviar la notificación. Servicio de Notificaciones no disponible");
    }
}
