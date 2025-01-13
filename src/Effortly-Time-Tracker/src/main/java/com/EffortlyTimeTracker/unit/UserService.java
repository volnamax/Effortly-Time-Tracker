package com.EffortlyTimeTracker.unit;

import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.exception.user.UserNotRoleException;
import com.EffortlyTimeTracker.repository.IUserRepository;
import com.EffortlyTimeTracker.repository.postgres.RoleRepository;
import com.mongodb.lang.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final IUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    public UserService(IUserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       @Autowired(required = false) SequenceGeneratorService sequenceGeneratorService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.sequenceGeneratorService = sequenceGeneratorService;

    }

    public RoleEntity getRoleByName(String roleName) {
        return roleRepository.findByName(Role.valueOf(roleName));
    }


    public UserEntity addUser(@NonNull UserEntity userEntity) {
        // Получаем роль пользователя по имени
        RoleEntity role = getRoleByName(userEntity.getRole().getName().name());
        if (role == null) {
            throw new UserNotRoleException();
        }
        userEntity.setRole(role);

        // Хэшируем пароль пользователя
        userEntity.setPasswordHash(passwordEncoder.encode(userEntity.getPasswordHash()));

        if (sequenceGeneratorService != null) {
            // Если активен профиль 'mongo', используем SequenceGeneratorService
            userEntity.setUserId((int) sequenceGeneratorService.generateSequence(UserEntity.class.getSimpleName()));
        } else {
            // Если активен профиль 'postgres', полагаемся на автоинкремент в БД
            // Не устанавливаем userId вручную
            userEntity.setUserId(null); // Или просто не задаем это поле
        }

        // Сохраняем пользователя в базе данных
        return userRepository.save(userEntity);
    }

    public void delUserById(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoudException(id);
        }
        userRepository.deleteById(id);
    }

    public UserEntity getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoudException(id));
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        return userRepository.findByEmail(email);
    }

}
