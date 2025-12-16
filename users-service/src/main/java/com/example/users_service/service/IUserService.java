package com.example.users_service.service;

import com.example.users_service.dto.UserCreateDTO;
import com.example.users_service.dto.UserEditDTO;
import com.example.users_service.model.User;

import java.util.List;

public interface IUserService {
    // Metodo para crear usuarios
    public User createUser(UserCreateDTO userCreateDTO);

    // Metodo para listar todos los usuarios
    public List<User> getAllUsers();

    // Metodo para obtener un usuario por su id
    public User getByIdUser(Long idUser);

    // Metodo para eliminar un usuario por su id
    public void deleteUser(Long idUser);

    // Metodo para editar un usuario
    public User editUser(Long idUser, UserEditDTO userEditDTO);
}
