package com.EffortlyTimeTracker.repository.mongo;

import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.entity.UserMongoEntity;
import com.EffortlyTimeTracker.repository.IUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository("userMongoRepository")  // Переименовываем бин для Mongo
public interface UserMongoRepository extends MongoRepository<UserMongoEntity, String>, IMongoUserRepository {
    Optional<UserMongoEntity> findByEmail(String email);
}
