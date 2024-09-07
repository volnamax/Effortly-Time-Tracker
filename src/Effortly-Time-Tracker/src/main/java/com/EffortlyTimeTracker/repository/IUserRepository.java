package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    Optional<UserEntity> findByEmail(String email);
    UserEntity save(UserEntity userEntity);
    void deleteById(Integer id);
    Optional<UserEntity> findById(Integer id);
    boolean existsById(Integer id);
    List<UserEntity> findAll();

}