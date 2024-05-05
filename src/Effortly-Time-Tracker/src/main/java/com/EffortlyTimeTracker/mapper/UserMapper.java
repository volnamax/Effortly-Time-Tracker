//package com.EffortlyTimeTracker.mapper;
//
//import com.EffortlyTimeTracker.DTO.userDTO.UserCreateDTO;
//import com.EffortlyTimeTracker.entity.RoleEntity;
//import com.EffortlyTimeTracker.entity.UserEntity;
//import lombok.extern.slf4j.Slf4j;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//
//@Component
//public class UserMapper {
//
//    public UserEntity dtoToEntityCreate(UserCreateDTO userCreateDTO, RoleEntity roleEntity) {
//        UserEntity userEntity = new UserEntity();
//
//        userEntity.setUserName(userCreateDTO.getUserName());
//        userEntity.setUserSecondname(userCreateDTO.getUserSecondname());
//        userEntity.setEmail(userCreateDTO.getEmail());
//        userEntity.setPasswordHash(userCreateDTO.getPasswordHash());
//        userEntity.setRole(roleEntity);
//
//
//        userEntity.setDataSignIn(userCreateDTO.getDataSignIn());
//
//        return userEntity;
//    }
//
//    public UserCreateDTO entityToCreateDto(UserEntity userEntity) {
//        UserCreateDTO userDTO = new UserCreateDTO();
//        userDTO.setUserName(userEntity.getUserName());
//        userDTO.setUserSecondname(userEntity.getUserSecondname());
//        userDTO.setEmail(userEntity.getEmail());
//        userDTO.setPasswordHash(userEntity.getPasswordHash());
//
//        if (userEntity.getRole() != null) {
//            userDTO.setRole(userEntity.getRole().getName().toString());
//        } else {
//            userDTO.setRole("GUEST");
//        }
//
//        userDTO.setDataSignIn(userEntity.getDataSignIn());
//        return userDTO;
//    }
//}


package com.EffortlyTimeTracker.mapper;

import com.EffortlyTimeTracker.DTO.TagDTO;
import com.EffortlyTimeTracker.DTO.userDTO.UserCreateDTO;
import com.EffortlyTimeTracker.DTO.userDTO.UserResponseDTO;
import com.EffortlyTimeTracker.entity.TagEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", ignore = true) // Игнорируем ID, так как он генерируется базой данных
    @Mapping(source = "role", target = "role.name") // Особое внимание к маппингу поля role
    UserEntity toEntity(UserCreateDTO dto);

    @Mapping(source = "role.name", target = "role") // Обратное преобразование для role
    UserCreateDTO toDTO(UserEntity entity);

    @Mapping(source = "role.name", target = "role") // Обратное преобразование для role
    UserResponseDTO toDTOResponse(UserEntity entity);

    List<UserResponseDTO> toDtoListResponse(List<UserEntity> entities);

}
