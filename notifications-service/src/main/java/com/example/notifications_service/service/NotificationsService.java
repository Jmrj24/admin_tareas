package com.example.notifications_service.service;

import com.example.notifications_service.dto.UserDTO;
import com.example.notifications_service.exception.FailedExceptionNotifications;
import com.example.notifications_service.exception.ServiceUnavailableExceptionNotifications;
import com.example.notifications_service.infrastructure.ExternalApiService;
import com.example.notifications_service.model.Notifications;
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationsService implements INotificationsService{


    @Autowired
    ExternalApiService externalApiService;

    @Autowired
    private MailtrapClient mailtrapClient;

    @Value("${mailtrap.sender.email}")
    private String senderEmail;

    @Value("${mailtrap.sender.name}")
    private String senderName;


    @Override
    public void sendNotification(Notifications notifications) {
        UserDTO userDTO = externalApiService.getUserById(notifications.getIdUser());

        MailtrapMail mail = MailtrapMail.builder()
                .from(new Address(senderEmail, senderName))
                // Aqui para que el correo se envia a cada usuario se debe setear en new Address "userDTO.getMail()"
                .to(List.of(new Address("jeferson24j@gmail.com" /*userDTO.getMail()*/)))
                .subject(notifications.getSubject())
                .text("Hola " + userDTO.getName()+ " " + notifications.getText())
                .category("Notification")
                .build();

        try {
            if(!mailtrapClient.send(mail).isSuccess()) {
                throw new FailedExceptionNotifications("Mail rechazado por Mailtrap");
            }
        } catch (Exception ex) {
            throw new ServiceUnavailableExceptionNotifications("Servicio de email no disponible");
        }


    }
}
