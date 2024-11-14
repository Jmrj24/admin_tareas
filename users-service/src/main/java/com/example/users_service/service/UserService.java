package com.example.users_service.service;

import com.example.users_service.dto.TaskDTO;
import com.example.users_service.dto.UserDTO;
import com.example.users_service.dto.UserTaskDTO;
import com.example.users_service.model.User;
import com.example.users_service.repository.IApiTask;
import com.example.users_service.repository.IUserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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
    public void createUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setMail(userDTO.getMail());
        user.setDateCreation(LocalDate.now());
        userRepo.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getByIdUser(Long idUser) {
        return userRepo.findById(idUser).orElse(null);
    }

    @Override
    public void deleteUser(Long idUser) {
        userRepo.deleteById(idUser);
    }

    @Override
    public void editUser(User user) {
        userRepo.save(user);
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
