package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.UserEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("userPostgresRepository")
  // Переименовываем бин для PostgreSQL
public interface UserPostgresRepository extends JpaRepository<UserEntity, Integer>, IUserRepository {

    // Метод для поиска пользователя по email
    Optional<UserEntity> findByEmail(String email);
}