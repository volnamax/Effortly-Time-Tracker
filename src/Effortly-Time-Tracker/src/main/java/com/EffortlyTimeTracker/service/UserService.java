package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.DTO.UserDTO;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.repository.UserRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(@NonNull UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity addUser(UserDTO userDTO) {
        Role role = Role.valueOf(userDTO.getRole().toUpperCase());

        UserEntity user = userRepository.save(UserEntity.builder()
                .userName(userDTO.getUserName())
                .userSecondname(userDTO.getUserSecondname())
                .email(userDTO.getEmail())
                .role(role)
                .dataLastLogin(userDTO.getDataLastLogin())
                .dataSignIn(userDTO.getDataSignIn())
                .build());
        log.info("New" + user);
        return user;
    }

    public void delUserById(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoudException(id);
        }
        userRepository.deleteById(id);
        log.info("user with id {} delete", id);
    }

    public UserEntity getUserById(Integer id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoudException(id));
        log.info("Get = " + user);

        return user;
    }

    public List<UserEntity> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        log.info("GetALL = " + users);
        return users;
    }

}
