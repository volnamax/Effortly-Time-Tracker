package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.entity.UserMongoEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.exception.user.UserNotRoleException;
import com.EffortlyTimeTracker.mapper.UserMongoMapper;
import com.EffortlyTimeTracker.repository.IUserRepository;
import com.EffortlyTimeTracker.repository.RoleRepository;
import com.EffortlyTimeTracker.repository.mongo.IMongoUserRepository;
import com.mongodb.lang.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final IUserRepository userPostgresRepository;
    private final IMongoUserRepository userMongoRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;  // Внедрение PasswordEncoder
    private final String activeDb;

    @Autowired
    public UserService(
            @Qualifier("userPostgresRepository") IUserRepository userPostgresRepository,
            @Qualifier("userMongoRepository") IMongoUserRepository userMongoRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.active-db}") String activeDb) {
        this.userPostgresRepository = userPostgresRepository;
        this.userMongoRepository = userMongoRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.activeDb = activeDb;
    }

    // Метод для получения роли по имени
    public RoleEntity getRoleByName(String roleName) {
        return roleRepository.findByName(Role.valueOf(roleName));
    }

    // Метод для добавления пользователя
    public UserEntity addUser(@NonNull UserEntity userEntity) {
        // Получаем роль
        RoleEntity role = getRoleByName(userEntity.getRole().getName().name());
        if (role == null) {
            throw new UserNotRoleException(); // Если роль не найдена, выбрасываем исключение
        }
        userEntity.setRole(role);  // Устанавливаем роль пользователю
        userEntity.setPasswordHash(passwordEncoder.encode(userEntity.getPasswordHash())); // Шифрование пароля

        if ("postgres".equalsIgnoreCase(activeDb)) {
            // Сохранение пользователя в PostgreSQL
            return userPostgresRepository.save(userEntity);
        } else if ("mongo".equalsIgnoreCase(activeDb)) {
            // Преобразование в Mongo сущность и сохранение в MongoDB
            UserMongoEntity mongoEntity = UserMongoMapper.toUserMongoEntity(userEntity);
            return UserMongoMapper.toUserEntity(userMongoRepository.save(mongoEntity));
        } else {
            throw new IllegalArgumentException("Unknown database type: " + activeDb);
        }
    }

    // Метод для удаления пользователя по ID
    public void delUserById(Integer id) {
        if ("postgres".equalsIgnoreCase(activeDb)) {
            if (!userPostgresRepository.existsById(id)) {
                throw new UserNotFoudException(id); // Исключение, если пользователь не найден
            }
            userPostgresRepository.deleteById(id);
        } else if ("mongo".equalsIgnoreCase(activeDb)) {
            if (!userMongoRepository.existsById(id.toString())) {
                throw new UserNotFoudException(id); // Исключение, если пользователь не найден
            }
            userMongoRepository.deleteById(id.toString());
        } else {
            throw new IllegalArgumentException("Unknown database type: " + activeDb);
        }
    }

    // Метод для получения пользователя по ID
    public UserEntity getUserById(Integer id) {
        if ("postgres".equalsIgnoreCase(activeDb)) {
            return userPostgresRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoudException(id));
        } else if ("mongo".equalsIgnoreCase(activeDb)) {
            return userMongoRepository.findById(id.toString())
                    .map(UserMongoMapper::toUserEntity)
                    .orElseThrow(() -> new UserNotFoudException(id));
        } else {
            throw new IllegalArgumentException("Unknown database type: " + activeDb);
        }
    }

    // Метод для получения всех пользователей
    public List<UserEntity> getAllUsers() {
        if ("postgres".equalsIgnoreCase(activeDb)) {
            return userPostgresRepository.findAll();
        } else if ("mongo".equalsIgnoreCase(activeDb)) {
            return userMongoRepository.findAll().stream()
                    .map(UserMongoMapper::toUserEntity)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Unknown database type: " + activeDb);
        }
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
}
