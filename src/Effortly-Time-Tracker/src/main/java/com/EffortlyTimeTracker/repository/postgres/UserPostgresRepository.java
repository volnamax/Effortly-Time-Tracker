package com.EffortlyTimeTracker.repository.postgres;

import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.repository.IUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("postgres")  // Переименовываем бин для PostgreSQL
public interface UserPostgresRepository extends JpaRepository<UserEntity, Integer>, IUserRepository {
    @Override
    // Метод для поиска пользователя по email
    Optional<UserEntity> findByEmail(String email);
}