package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final IUserRepository userRepository;

    @Autowired
    public UserService(
            @Qualifier("userPostgresRepository") IUserRepository userPostgresRepository,
            @Qualifier("userMongoRepository") IUserRepository userMongoRepository,
            @Value("${app.active-db}") String activeDb) {

        if ("postgres".equalsIgnoreCase(activeDb)) {
            this.userRepository = userPostgresRepository;
        } else if ("mongo".equalsIgnoreCase(activeDb)) {
            this.userRepository = userMongoRepository;
        } else {
            throw new IllegalArgumentException("Unknown database type: " + activeDb);
        }
    }

    public Optional<UserEntity> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity addUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    // Добавляем метод для удаления пользователя по ID
    public void delUserById(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    public UserEntity getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
}
