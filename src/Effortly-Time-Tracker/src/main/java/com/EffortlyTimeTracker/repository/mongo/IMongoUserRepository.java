package com.EffortlyTimeTracker.repository.mongo;

import com.EffortlyTimeTracker.entity.UserMongoEntity;

import java.util.List;
import java.util.Optional;

public interface IMongoUserRepository {
    Optional<UserMongoEntity> findByEmail(String email);
    UserMongoEntity save(UserMongoEntity userEntity);
    void deleteById(String id);
    Optional<UserMongoEntity> findById(String id);
    boolean existsById(String id);
    List<UserMongoEntity> findAll();
}
