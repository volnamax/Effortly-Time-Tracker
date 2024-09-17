package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.exception.user.UserNotRoleException;
import com.EffortlyTimeTracker.repository.IUserRepository;
import com.EffortlyTimeTracker.repository.postgres.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private SequenceGeneratorService sequenceGeneratorService;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;
    private RoleEntity roleEntity;

    @Mock
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        roleEntity = new RoleEntity();
        roleEntity.setName(Role.MANAGER);

        userEntity.setUserId(1);
        userEntity.setUserName("TestUser");
        userEntity.setUserSecondname("TestSecondName");
        userEntity.setEmail("teweft@example.com");
        userEntity.setPasswordHash("password");
        userEntity.setRole(roleEntity);
    }


    @Test
    public void addUserTestSuccess() {
        when(roleRepository.findByName(Role.MANAGER)).thenReturn(roleEntity);
        when(sequenceGeneratorService.generateSequence(anyString())).thenReturn(1L);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity savedUser = userService.addUser(userEntity);

        assertNotNull(savedUser);
        assertEquals("TestUser", savedUser.getUserName());
        verify(userRepository).save(any(UserEntity.class));  // Verify userRepository.save() was called
    }


    @Test
    public void AddUserTestRepositoryThrowsException() {
        when(roleRepository.findByName(any())).thenReturn(roleEntity);
        when(sequenceGeneratorService.generateSequence(anyString())).thenReturn(1L);

        when(userRepository.save(any(UserEntity.class))).thenThrow(new RuntimeException("Failed to save"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.addUser(userEntity);
        });

        assertEquals("Failed to save", exception.getMessage());
    }

    @Test
    public void addUserTestNullRole() {
        // Имитируем, что роль не найдена
        when(roleRepository.findByName(any())).thenReturn(null);

        // Проверяем, что метод addUser выбрасывает UserNotRoleException
        Exception exception = assertThrows(UserNotRoleException.class, () -> {
            userService.addUser(userEntity);
        });

        // Проверяем, что сообщение исключения совпадает с ожидаемым
        assertNotNull(exception);
        assertEquals("User with id not found role", exception.getMessage());

        // Проверяем, что метод поиска роли был вызван
        verify(roleRepository).findByName(any());
    }

    @Test
    public void deleteUserByIdTestExists() {
        when(userRepository.existsById(anyInt())).thenReturn(true);

        userService.delUserById(1);

        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    public void deleteUserByIdTestNotExists() {
        when(userRepository.existsById(anyInt())).thenReturn(false);

        Exception exception = assertThrows(UserNotFoudException.class, () -> {
            userService.delUserById(1);
        });

        assertEquals("User with id 1 not found", exception.getMessage());
        verify(userRepository, never()).deleteById(anyInt());
    }

    @Test
    public void getUserByIdTestFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userEntity));

        UserEntity foundUser = userService.getUserById(1);

        assertNotNull(foundUser);
        assertEquals("TestUser", foundUser.getUserName());
        assertEquals(1, foundUser.getUserId());

    }

    @Test
    public void getUserByIdTestNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoudException.class, () -> {
            userService.getUserById(1);
        });

        assertEquals("User with id 1 not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    public void getUserByEmailTestSuccess() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));

        // Вызов метода
        Optional<UserEntity> foundUser = userService.getUserByEmail("teweft@example.com");

        // Проверки
        assertTrue(foundUser.isPresent());
        assertEquals("TestUser", foundUser.get().getUserName());
        assertEquals("teweft@example.com", foundUser.get().getEmail());

        // Проверяем, что метод findByEmail был вызван
        verify(userRepository, times(1)).findByEmail("teweft@example.com");
    }

    @Test
    public void getUserByEmailTestNotFound() {
        // Настройка: возвращаем пустой Optional, если пользователя нет
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Вызов метода
        Optional<UserEntity> foundUser = userService.getUserByEmail("notfound@example.com");

        // Проверки
        assertFalse(foundUser.isPresent());

        // Проверяем, что метод findByEmail был вызван
        verify(userRepository, times(1)).findByEmail("notfound@example.com");
    }

    @Test
    public void getUserByEmailTestNullEmail() {
        // Проверяем, что при null выбрасывается IllegalArgumentException
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserByEmail(null);
        });

        // Проверка сообщения исключения
        assertEquals("Email cannot be null", exception.getMessage());

        // Проверяем, что метод findByEmail не был вызван
        verify(userRepository, never()).findByEmail(anyString());
    }

}