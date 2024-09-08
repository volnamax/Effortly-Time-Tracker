package com.EffortlyTimeTracker.repository.mongo;

import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.repository.IUserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@Profile("mongo")// Переименовываем бин для Mongo
public interface UserMongoRepository extends MongoRepository<UserEntity, Integer>, IUserRepository {
    Optional<UserEntity> findByEmail(String email);
}
