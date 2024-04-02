package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.DTO.UserDTO;
import com.EffortlyTimeTracker.entity.User;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(UserDTO userDTO) {
        User user = userRepository.save(User.builder()
                        .userName(userDTO.getUserName())
                        .userSecondname(userDTO.getUserSecondname())
                        .email(userDTO.getEmail())
                        .description(userDTO.getDescription())
                        .role(User.Role.valueOf(userDTO.getRole()))
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

    public User getUserById(Integer id) {
        User user  = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoudException(id));
        log.info("Get = " + user);

        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        log.info("GetALL = " + users);
        return users;
    }



}
