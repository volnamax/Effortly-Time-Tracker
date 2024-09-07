package com.EffortlyTimeTracker.repository.mongo;

import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.repository.IUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository("userMongoRepository")
@Profile("mongo")  // Репозиторий активен только при профиле mongo
public interface UserMongoRepository extends MongoRepository<UserEntity, String>, IUserRepository {

    // Метод для поиска пользователя по email
    Optional<UserEntity> findByEmail(String email);
}