package com.example.users_service.service;

import com.example.users_service.dto.UserCreateDTO;
import com.example.users_service.dto.UserEditDTO;
import com.example.users_service.exception.ConflictException;
import com.example.users_service.exception.NotFoundException;
import com.example.users_service.model.User;
import com.example.users_service.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepo;

    @Override
    public User createUser(UserCreateDTO userCreateDTO) {
        validMail(userCreateDTO.getMail());
        User user = new User();
        user.setName(userCreateDTO.getName());
        user.setMail(userCreateDTO.getMail());
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
    public User editUser(Long idUser, UserEditDTO userEditDTO) {
        User userEdit = getByIdUser(idUser);
        if(userEditDTO.getName()!=null) {
            userEdit.setName(userEditDTO.getName());
        }
        if(userEditDTO.getMail()!=null) {
            validMail(userEditDTO.getMail());
            userEdit.setMail(userEditDTO.getMail());
        }
        return userRepo.save(userEdit);
    }

    public void validMail(String mail) {
        if(userRepo.findByMail(mail).isPresent()) {
            throw new ConflictException("Mail: " + mail + " ya se encuentra registrado");
        }
    }
}
