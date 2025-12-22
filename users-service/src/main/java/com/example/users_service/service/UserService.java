package com.example.users_service.service;

import com.example.users_service.dto.UserCreateDTO;
import com.example.users_service.dto.UserEditDTO;
import com.example.users_service.dto.UserResponseDTO;
import com.example.users_service.exception.ConflictExceptionUser;
import com.example.users_service.exception.NotFoundExceptionUser;
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
    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
        validMail(userCreateDTO.mail());
        User user = new User();
        user.setName(userCreateDTO.name());
        user.setMail(userCreateDTO.mail());
        user.setDateCreation(LocalDate.now());
        User userSaved = userRepo.save(user);
        return toUserResponse(userSaved);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(this::toUserResponse)
                .toList();
    }

    @Override
    public User getByIdEntityUser(Long idUser) {
        return userRepo.findById(idUser).orElseThrow(() -> new NotFoundExceptionUser("Usuario no Encontrado"));
    }

    @Override
    public UserResponseDTO getByIdUser(Long idUser) {
        return toUserResponse(getByIdEntityUser(idUser));
    }

    @Override
    public void deleteUser(Long idUser) {
        userRepo.deleteById(idUser);
    }

    @Override
    public UserResponseDTO editUser(Long idUser, UserEditDTO userEditDTO) {
        User userEdit = getByIdEntityUser(idUser);
        if(userEditDTO.getName()!=null) {
            userEdit.setName(userEditDTO.getName());
        }
        if(userEditDTO.getMail()!=null) {
            validMail(userEditDTO.getMail());
            userEdit.setMail(userEditDTO.getMail());
        }
        User userSaved = userRepo.save(userEdit);
        return toUserResponse(userSaved);
    }

    private void validMail(String mail) {
        if(userRepo.findByMail(mail).isPresent()) {
            throw new ConflictExceptionUser("Mail: " + mail + " ya se encuentra registrado");
        }
    }

    private UserResponseDTO toUserResponse(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getMail(), user.getDateCreation());
    }
}
