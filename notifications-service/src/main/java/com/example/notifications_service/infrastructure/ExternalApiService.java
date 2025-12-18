package com.example.notifications_service.infrastructure;

import com.example.notifications_service.dto.UserDTO;
import com.example.notifications_service.exception.NotFoundExceptionNotifications;
import com.example.notifications_service.exception.ServiceUnavailableExceptionNotifications;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExternalApiService {
    @Autowired
    private IApiUser apiUser;

    @CircuitBreaker(name = "users-service", fallbackMethod = "fallbackMethodGetUserById")
    @Retry(name = "users-service")
    public UserDTO getUserById(Long idUser) {
        return apiUser.getUserById(idUser);
    }

    public UserDTO fallbackMethodGetUserById(Long idUser, Throwable throwable) {
        // Manejo de errores Feign para distinguir 404 (negocio) de errores de disponibilidad del servicio
        if (throwable instanceof FeignException feign) {
            if(feign.status()==404) {
                throw new NotFoundExceptionNotifications("Usuario No existe");
            }
            throw new ServiceUnavailableExceptionNotifications("No se pudo verificar el usuario con id: " + idUser + ". Servicio de usuarios no disponible");
        }
        throw new ServiceUnavailableExceptionNotifications("Error inesperado");
    }
}
