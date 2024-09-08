package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.entity.UserMongoEntity;
import com.EffortlyTimeTracker.mapper.UserMongoMapper;
import com.EffortlyTimeTracker.repository.IUserRepository;
import com.EffortlyTimeTracker.repository.mongo.IMongoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final IUserRepository userPostgresRepository;
    private final IMongoUserRepository userMongoRepository;
    private final String activeDb;

    @Autowired
    public UserService(
            @Qualifier("userPostgresRepository") IUserRepository userPostgresRepository,
            @Qualifier("userMongoRepository") IMongoUserRepository userMongoRepository,
            @Value("${app.active-db}") String activeDb) {
        this.userPostgresRepository = userPostgresRepository;
        this.userMongoRepository = userMongoRepository;
        this.activeDb = activeDb;
    }

    // Метод для получения пользователя по email
    public Optional<UserEntity> getUserByEmail(String email) {
        if ("postgres".equalsIgnoreCase(activeDb)) {
            return userPostgresRepository.findByEmail(email);
        } else if ("mongo".equalsIgnoreCase(activeDb)) {
            return userMongoRepository.findByEmail(email)
                    .map(UserMongoMapper::toUserEntity);
        } else {
            throw new IllegalArgumentException("Unknown database type: " + activeDb);
        }
    }

    // Метод для добавления пользователя
    public UserEntity addUser(UserEntity userEntity) {
        if ("postgres".equalsIgnoreCase(activeDb)) {
            return userPostgresRepository.save(userEntity);
        } else if ("mongo".equalsIgnoreCase(activeDb)) {
            UserMongoEntity mongoEntity = UserMongoMapper.toUserMongoEntity(userEntity);
            return UserMongoMapper.toUserEntity(userMongoRepository.save(mongoEntity));
        } else {
            throw new IllegalArgumentException("Unknown database type: " + activeDb);
        }
    }

    // Метод для удаления пользователя по ID
    public void delUserById(Integer id) {
        if ("postgres".equalsIgnoreCase(activeDb)) {
            Integer postgresId = Integer.parseInt(id.toString());  // Преобразуем ID в Integer для PostgreSQL
            if (!userPostgresRepository.existsById(postgresId)) {
                throw new IllegalArgumentException("User with id " + postgresId + " not found");
            }
            userPostgresRepository.deleteById(postgresId);
        } else if ("mongo".equalsIgnoreCase(activeDb)) {
            if (!userMongoRepository.existsById(id.toString())) {
                throw new IllegalArgumentException("User with id " + id + " not found");
            }
            userMongoRepository.deleteById(id.toString());
        } else {
            throw new IllegalArgumentException("Unknown database type: " + activeDb);
        }
    }

    // Метод для получения пользователя по ID
    public UserEntity getUserById(Integer id) {
        if ("postgres".equalsIgnoreCase(activeDb)) {
            Integer postgresId = Integer.parseInt(id.toString());  // Преобразуем ID в Integer для PostgreSQL
            return userPostgresRepository.findById(postgresId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
        } else if ("mongo".equalsIgnoreCase(activeDb)) {
            return userMongoRepository.findById(id.toString())
                    .map(UserMongoMapper::toUserEntity)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
        } else {
            throw new IllegalArgumentException("Unknown database type: " + activeDb);
        }
    }

    // Метод для получения всех пользователей
    public List<UserEntity> getAllUsers() {
        if ("postgres".equalsIgnoreCase(activeDb)) {
            return userPostgresRepository.findAll().stream()
                    .toList();
        } else if ("mongo".equalsIgnoreCase(activeDb)) {
            return userMongoRepository.findAll().stream()
                    .map(UserMongoMapper::toUserEntity)
                    .toList();
        } else {
            throw new IllegalArgumentException("Unknown database type: " + activeDb);
        }
    }
}

