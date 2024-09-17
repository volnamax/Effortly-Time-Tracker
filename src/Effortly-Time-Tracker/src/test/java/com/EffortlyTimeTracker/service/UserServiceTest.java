package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.builder.RoleEntityBuilder;
import com.EffortlyTimeTracker.builder.UserEntityBuilder;
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
        roleEntity = new RoleEntityBuilder()
                .withRole(Role.MANAGER)
                .build();

        userEntity = new UserEntityBuilder()
                .withUserId(1)
                .withUserName("TestUser")
                .withUserSecondName("TestSecondName")
                .withEmail("teweft@example.com")
                .withPasswordHash("password")
                .withRole(roleEntity)
                .build();
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
        when(roleRepository.findByName(any())).thenReturn(null);

        Exception exception = assertThrows(UserNotRoleException.class, () -> {
            userService.addUser(userEntity);
        });

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

        Optional<UserEntity> foundUser = userService.getUserByEmail("teweft@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("TestUser", foundUser.get().getUserName());
        assertEquals("teweft@example.com", foundUser.get().getEmail());

        // Проверяем, что метод findByEmail был вызван
        verify(userRepository, times(1)).findByEmail("teweft@example.com");
    }

    @Test
    public void getUserByEmailTestNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Optional<UserEntity> foundUser = userService.getUserByEmail("notfound@example.com");

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findByEmail("notfound@example.com");
    }

    @Test
    public void getUserByEmailTestNullEmail() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserByEmail(null);
        });

        assertEquals("Email cannot be null", exception.getMessage());
        verify(userRepository, never()).findByEmail(anyString());
    }

}