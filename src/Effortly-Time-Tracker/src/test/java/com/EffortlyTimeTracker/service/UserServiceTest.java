package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;
    private RoleEntity roleEntity;


    @Test
    void getRoleByName() {
    }

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        roleEntity = new RoleEntity();
        roleEntity.setName(Role.USER);

        userEntity.setUserId(1);
        userEntity.setUserName("TestUser");
        userEntity.setUserSecondname("TestSecondName");
        userEntity.setEmail("teweft@example.com");
        userEntity.setPasswordHash("password");
        userEntity.setRole(roleEntity);

    }

    @Test
    public void addUserTestSuccess() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity savedUser = userService.addUser(userEntity);
        assertNotNull(savedUser);
        assertEquals("TestUser", savedUser.getUserName());

        verify(userRepository).save(userEntity);
    }

    @Test
    public void addUserTestNullUser() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            userService.addUser(null);
        });

        String expectedMessage = "userEntity is marked non-null but is null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void AddUserTestRepositoryThrowsException() {
        when(userRepository.save(any(UserEntity.class))).thenThrow(new RuntimeException("Failed to save"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.addUser(userEntity);
        });

        assertEquals("Failed to save", exception.getMessage());
    }

    @Test
    public void deleteUserByIdTestExists() {
        when(userRepository.existsById(anyInt())).thenReturn(true);

        userService.delUserById(1);

        verify(userRepository).deleteById(1);
    }

    @Test
    public void deleteUserByIdTestNotExists() {
        when(userRepository.existsById(anyInt())).thenReturn(false);

        Exception exception = assertThrows(UserNotFoudException.class, () -> {
            userService.delUserById(1);
        });

        assertEquals("User with id 1 not found", exception.getMessage());
    }

    @Test
    public void getUserByIdTestFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userEntity));

        UserEntity foundUser = userService.getUserById(1);

        assertNotNull(foundUser);
        assertEquals("TestUser", foundUser.getUserName());
    }

    @Test
    public void getUserByIdTestNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoudException.class, () -> {
            userService.getUserById(1);
        });

        assertEquals("User with id 1 not found", exception.getMessage());
    }

}