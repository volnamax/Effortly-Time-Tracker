package com.EffortlyTimeTracker.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.EffortlyTimeTracker.DTO.UserDTO;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.repository.UserRepository;
import com.EffortlyTimeTracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setUserName("TestUser");
        userDTO.setUserSecondname("TestSecondName");
        userDTO.setEmail("teweft@example.com");
        userDTO.setRole("ADMIN");
    }

    @Test
    void addUser() {
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.addUser(userDTO);

        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void deleteUserById_whenUserExists_thenSuccessfullyDeleted() {
        Integer existingUserId = 1;
        when(userRepository.existsById(existingUserId)).thenReturn(true);

        userService.delUserById(existingUserId);

        verify(userRepository).deleteById(existingUserId);
    }



    @Test
    void deleteUserById_whenUserDoesNotExist_thenThrowException() {
        Integer nonExistentUserId = 999;
        when(userRepository.existsById(nonExistentUserId)).thenReturn(false);

        Exception exception = assertThrows(UserNotFoudException.class, () -> userService.delUserById(nonExistentUserId));

        assertTrue(exception.getMessage().contains(String.valueOf(nonExistentUserId)));
        verify(userRepository, never()).deleteById(anyInt());
    }

    @Test
    void getUserById_whenUserExists() {
        Integer userId = 1;
        UserEntity mockUser = new UserEntity();
        mockUser.setUserId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        UserEntity foundUser = userService.getUserById(userId);

        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getUserId());
    }

    @Test
    void getUserById_whenUserDoesNotExist_thenThrowException() {
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoudException.class, () -> {
            userService.getUserById(userId);
        });
        assertTrue(exception.getMessage().contains("User with id "  + userId  + " not found"));
    }


    @Test
    void getAllUsers_returnsListOfUsers() {
        UserEntity user1 = new UserEntity(); // Assuming User class has appropriate setters or constructor
        UserEntity user2 = new UserEntity();
        List<UserEntity> mockUsers = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<UserEntity> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAllUsers_returnsEmptyListWhenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        List<UserEntity> users = userService.getAllUsers();

        assertNotNull(users);
        assertTrue(users.isEmpty());
        verify(userRepository, times(1)).findAll();
    }


}
