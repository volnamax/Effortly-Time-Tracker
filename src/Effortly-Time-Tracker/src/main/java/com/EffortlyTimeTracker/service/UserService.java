package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.exception.user.UserNotRoleException;
import com.EffortlyTimeTracker.repository.RoleRepository;
import com.EffortlyTimeTracker.repository.UserRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j

public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder; // Внедрение PasswordEncoder


    @Autowired
    public UserService(@NonNull UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder; // Внедрение PasswordEncoder
    }


    public RoleEntity getRoleByName(String roleName) {
        return roleRepository.findByName(Role.valueOf(roleName));
    }

    public UserEntity addUser(@NonNull UserEntity userEntity) {
        RoleEntity role = getRoleByName(userEntity.getRole().getName().name());
        if (role == null) {
            throw new UserNotRoleException();
        }
        userEntity.setRole(role);
        userEntity.setPasswordHash(passwordEncoder.encode(userEntity.getPasswordHash())); // Шифрование пароля

        return userRepository.save(userEntity);

    }

    public void delUserById(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoudException(id);
        }
        userRepository.deleteById(id);
    }

    public UserEntity getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoudException(id));
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
    public Optional<UserEntity> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
