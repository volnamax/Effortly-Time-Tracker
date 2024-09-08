package com.EffortlyTimeTracker.mapper;

import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.entity.UserMongoEntity;
import com.EffortlyTimeTracker.enums.Role;


public class UserMongoMapper {

    // Преобразование из UserMongoEntity в UserEntity
    public static UserEntity toUserEntity(UserMongoEntity mongoEntity) {
        return UserEntity.builder()
                .userId(null)  // ID будет null, если оно не нужно
                .userName(mongoEntity.getUserName())
                .userSecondname(mongoEntity.getUserSecondName())
                .email(mongoEntity.getEmail())
                .passwordHash(mongoEntity.getPasswordHash())
                // Преобразование строки в RoleEntity с использованием перечисления Role
                .role(new RoleEntity(null, Role.valueOf(mongoEntity.getRole())))
                .dataSignIn(mongoEntity.getDataSignIn())
                .dataLastLogin(mongoEntity.getDataLastLogin())
                .build();
    }

    // Преобразование из UserEntity в UserMongoEntity
    public static UserMongoEntity toUserMongoEntity(UserEntity userEntity) {
        return UserMongoEntity.builder()
                .id(userEntity.getUserId().toString())
                .userName(userEntity.getUserName())
                .userSecondName(userEntity.getUserSecondname())
                .email(userEntity.getEmail())
                .passwordHash(userEntity.getPasswordHash())
                // Преобразование RoleEntity в строку
                .role(userEntity.getRole().getName().name())
                .dataSignIn(userEntity.getDataSignIn())
                .dataLastLogin(userEntity.getDataLastLogin())
                .build();
    }
}
