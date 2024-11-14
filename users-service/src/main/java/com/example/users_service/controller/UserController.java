package com.example.users_service.controller;

import com.example.users_service.dto.UserDTO;
import com.example.users_service.dto.UserTaskDTO;
import com.example.users_service.model.User;
import com.example.users_service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userServ;

    @PostMapping("/create")
    public String saveUser(@RequestBody UserDTO userDTO) {
        userServ.createUser(userDTO);
        return "Usuario creado con exito!";
    }

    @GetMapping("/get")
    public List<User> getAllUsers() {
        return userServ.getAllUsers();
    }

    @DeleteMapping("/delete/{idUser}")
    public String deleteUser(@PathVariable Long idUser) {
        userServ.deleteUser(idUser);
        return "Usuario eliminado con exito!";
    }

    @PutMapping("/edit")
    public User editUser(@RequestBody User user) {
        userServ.editUser(user);
        return userServ.getByIdUser(user.getId());
    }

    @GetMapping("/get/task/{idUser}")
    public UserTaskDTO getAllTasksByUser(@PathVariable Long idUser) {
        return userServ.getAllTasksByUser(idUser);
    }

    @GetMapping("/get/{idUser}")
    public User getUserById(@PathVariable Long idUser) {
        return userServ.getByIdUser(idUser);
    }
}
