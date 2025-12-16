package com.example.notifications_service.service;

import com.example.notifications_service.dto.UserDTO;
import com.example.notifications_service.model.Notifications;
import com.example.notifications_service.repository.IApiUser;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationsService implements INotificationsService{
    /*@Autowired
    private JavaMailSender javaMailSender;*/

    @Autowired
    private IApiUser apiUser;

    @Autowired
    private MailtrapClient mailtrapClient;

    @Value("${mailtrap.sender.email}")
    private String senderEmail;

    @Value("${mailtrap.sender.name}")
    private String senderName;


    @Override
    public void sendNotification(Notifications notifications) {
        UserDTO userDTO = getUserById(notifications.getIdUser());

        MailtrapMail mail = MailtrapMail.builder()
                .from(new Address(senderEmail, senderName))
                // Aqui para que el correo se envia a cada usuario se debe setear en new Address "userDTO.getMail()"
                .to(List.of(new Address(/*"jeferson24j@gmail.com"*/ userDTO.getMail())))
                .subject(notifications.getSubject())
                .text("Hola " + userDTO.getName()+ " " + notifications.getText())
                .category("Notification")
                .build();

        try {
            //mailtrapClient.send(mail);
            System.out.println(mail);
        } catch (Exception e) {
            System.err.println("Error enviando email: " + e);
        }

        /*
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hello@demomailtrap.com");
        // Aqui para que el correo se envia a cada usuario se debe setear en setTo "userDTO.getMail()"
        message.setTo("jeferson24j@gmail.com");
        message.setSubject(notifications.getSubject());
        message.setText("Hola " + userDTO.getName()+ " " + notifications.getText());
        javaMailSender.send(message);
        */
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
