package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.exception.user.UserNotRoleException;
import com.EffortlyTimeTracker.repository.IUserRepository;
import com.EffortlyTimeTracker.repository.RoleRepository;
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
    private final PasswordEncoder passwordEncoder;  // Внедрение PasswordEncoder

    @Autowired
    public UserService(IUserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Метод для получения роли по имени
    public RoleEntity getRoleByName(String roleName) {
        return roleRepository.findByName(Role.valueOf(roleName));
    }

    // Метод для добавления пользователя
    public UserEntity addUser(@NonNull UserEntity userEntity) {
        RoleEntity role = getRoleByName(userEntity.getRole().getName().name());
        if (role == null) {
            throw new UserNotRoleException();  // Если роль не найдена, выбрасываем исключение
        }
        userEntity.setRole(role);  // Устанавливаем роль пользователю
        userEntity.setPasswordHash(passwordEncoder.encode(userEntity.getPasswordHash()));  // Шифрование пароля
        return userRepository.save(userEntity);  // Сохранение пользователя в выбранную базу данных
    }

    // Метод для удаления пользователя по ID
    public void delUserById(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoudException(id);  // Исключение, если пользователь не найден
        }
        userRepository.deleteById(id);
    }

    // Метод для получения пользователя по ID
    public UserEntity getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoudException(id));
    }

    // Метод для получения всех пользователей
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // Метод для получения пользователя по email
    public Optional<UserEntity> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
