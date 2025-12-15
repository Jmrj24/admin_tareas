package com.example.users_service.service;

import com.example.users_service.dto.UserDTO;
import com.example.users_service.dto.UserTaskDTO;
import com.example.users_service.exception.ConflictException;
import com.example.users_service.model.User;
import com.example.users_service.repository.IApiTask;
import com.example.users_service.repository.IUserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepo;

    @Autowired
    private IApiTask apiTask;

    @Override
    public User createUser(UserDTO userDTO) {
        if(userRepo.findByMail(userDTO.getMail()).isPresent()) {
            throw new ConflictException("Mail: " + userDTO.getMail() + " ya se encuentra registrado");
        }
        User user = new User();
        user.setName(userDTO.getName());
        user.setMail(userDTO.getMail());
        user.setDateCreation(LocalDate.now());
        return userRepo.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getByIdUser(Long idUser) {
        return userRepo.findById(idUser).orElseThrow(() -> new NotFoundException("Usuario no Encontrado"));
    }

    @Override
    public void deleteUser(Long idUser) {
        userRepo.deleteById(idUser);
    }

    @Override
    public User editUser(Long idUser, UserDTO userDTO) {
        if(!userRepo.existsById(idUser)) {
            throw new NotFoundException("Usuario no encontrado");
        }
        User userEdit = getByIdUser(idUser);
        userEdit.setName(userDTO.getName());
        userEdit.setMail(userDTO.getMail());
        return userRepo.save(userEdit);
    }

    @Override
    @CircuitBreaker(name = "tasks-service", fallbackMethod = "fallbackMethodGetAllTasksByUser")
    @Retry(name = "tasks-service")
    public UserTaskDTO getAllTasksByUser(Long idUser) {
        User user = this.getByIdUser(idUser);
        //createException();
        return new UserTaskDTO(user.getName(), user.getMail(), apiTask.getAllTasksByUser(idUser));
    }

    public UserTaskDTO fallbackMethodGetAllTasksByUser(Throwable throwable) {
        return new UserTaskDTO("Fallido","Fallido", null);
    }

    // Creamos una exception para probar el funcionamiento del Circuit Breaker
    public void createException() {
        throw new IllegalArgumentException("Prueba");
    }
}
