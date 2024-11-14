package com.example.notifications_service.service;

import com.example.notifications_service.dto.UserDTO;
import com.example.notifications_service.model.Notifications;
import com.example.notifications_service.repository.IApiUser;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NotificationsService implements INotificationsService{
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private IApiUser apiUser;

    @Override
    public void sendNotification(Notifications notifications) {
        UserDTO userDTO = getUserById(notifications.getIdUser());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hello@demomailtrap.com");
        // Aqui para que el correo se envia a cada usuario se debe setear en setTo "userDTO.getMail()"
        message.setTo("jeferson24j@gmail.com");
        message.setSubject(notifications.getSubject());
        message.setText("Hola " + userDTO.getName()+ " " + notifications.getText());
        javaMailSender.send(message);
    }

    @CircuitBreaker(name = "users-service", fallbackMethod = "fallbackMethodGetUserById")
    @Retry(name = "users-service")
    public UserDTO getUserById(Long idUser) {
        return apiUser.getUserById(idUser);
    }

    public UserDTO fallbackMethodGetUserById(Throwable throwable) {
        return new UserDTO(0L, "Fallido", "Fallido", LocalDate.of(1111,1,1));
    }
}
