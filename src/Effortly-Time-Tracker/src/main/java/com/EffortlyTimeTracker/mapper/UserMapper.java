package com.EffortlyTimeTracker.mapper;

import com.EffortlyTimeTracker.DTO.UserCreateDTO;
import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j

@Component
public class UserMapper {

    public UserEntity dtoToEntityCreate(UserCreateDTO userCreateDTO, RoleEntity roleEntity) {
        UserEntity userEntity = new UserEntity();

        userEntity.setUserName(userCreateDTO.getUserName());
        userEntity.setUserSecondname(userCreateDTO.getUserSecondname());
        userEntity.setEmail(userCreateDTO.getEmail());
        userEntity.setPasswordHash(userCreateDTO.getPasswordHash());
        userEntity.setRole(roleEntity);


        userEntity.setDataSignIn(userCreateDTO.getDataSignIn());

        return userEntity;
    }

    public UserCreateDTO entityToCreateDto(UserEntity userEntity) {
        UserCreateDTO userDTO = new UserCreateDTO();
        userDTO.setUserName(userEntity.getUserName());
        userDTO.setUserSecondname(userEntity.getUserSecondname());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setPasswordHash(userEntity.getPasswordHash());

        if (userEntity.getRole() != null) {
            userDTO.setRole(userEntity.getRole().getName().toString());
        } else {
            userDTO.setRole("GUEST");
        }

        userDTO.setDataSignIn(userEntity.getDataSignIn());
        return userDTO;
    }
}