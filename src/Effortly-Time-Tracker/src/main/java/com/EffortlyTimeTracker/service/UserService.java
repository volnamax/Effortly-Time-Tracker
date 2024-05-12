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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j

public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(@NonNull UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
}
