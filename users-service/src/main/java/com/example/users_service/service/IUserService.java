package com.example.users_service.service;

import com.example.users_service.dto.UserCreateDTO;
import com.example.users_service.dto.UserEditDTO;
import com.example.users_service.dto.UserResponseDTO;
import com.example.users_service.model.User;

import java.util.List;

public interface IUserService {
    // Metodo para crear usuarios
    public UserResponseDTO createUser(UserCreateDTO userCreateDTO);

    // Metodo para listar todos los usuarios
    public List<UserResponseDTO> getAllUsers();

    // Metodo para obtener una entidad de usuario por su id
    public User getByIdEntityUser(Long idUser);

    // Metodo para obtener un usuario por su id
    public UserResponseDTO getByIdUser(Long idUser);

    // Metodo para eliminar un usuario por su id
    public void deleteUser(Long idUser);

    // Metodo para editar un usuario
    public UserResponseDTO editUser(Long idUser, UserEditDTO userEditDTO);
}
