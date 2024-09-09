package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
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
      //  userService = new UserService(userRepository);

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
        when(roleRepository.findByName(Role.MANAGER)).thenReturn(roleEntity);  // Mock the behavior of roleRepository
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);  // Mock the behavior of userRepository

        UserEntity savedUser = userService.addUser(userEntity);
        assertNotNull(savedUser);
        assertEquals("TestUser", savedUser.getUserName());

        verify(userRepository).save(any(UserEntity.class));  // Verify userRepository.save() was called
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
        // Mock the roleRepository to return a valid role
        when(roleRepository.findByName(any())).thenReturn(roleEntity);

        // Now setup the userRepository to throw an exception as intended
        when(userRepository.save(any(UserEntity.class))).thenThrow(new RuntimeException("Failed to save"));

        // Execute the test
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.addUser(userEntity);
        });

        // Check the exception message to see if it's the one we expect from userRepository
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