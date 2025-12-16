package com.example.users_service.controller;

import com.example.users_service.dto.UserCreateDTO;
import com.example.users_service.dto.UserEditDTO;
import com.example.users_service.model.User;
import com.example.users_service.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userServ;

    @PostMapping("/create")
    public ResponseEntity<User> saveUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userServ.createUser(userCreateDTO));
    }

    @GetMapping("/get")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userServ.getAllUsers());
    }

    @DeleteMapping("/delete/{idUser}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long idUser) {
        userServ.deleteUser(idUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/edit/{idUser}")
    public ResponseEntity<User> editUser(@PathVariable Long idUser, @Valid @RequestBody UserEditDTO userEditDTO) {
        return ResponseEntity.ok(userServ.editUser(idUser, userEditDTO));
    }

    @GetMapping("/get/{idUser}")
    public ResponseEntity<User> getUserById(@PathVariable Long idUser) {
        return ResponseEntity.ok(userServ.getByIdUser(idUser));
    }
}
