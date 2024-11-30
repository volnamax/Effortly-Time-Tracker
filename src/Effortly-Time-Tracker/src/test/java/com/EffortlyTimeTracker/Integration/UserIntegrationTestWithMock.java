package com.EffortlyTimeTracker.Integration;

import com.EffortlyTimeTracker.builder.RoleEntityBuilder;
import com.EffortlyTimeTracker.builder.UserEntityBuilder;
import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.repository.IUserRepository;
import com.EffortlyTimeTracker.service.SequenceGeneratorService;
import com.EffortlyTimeTracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserIntegrationTestWithMock {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("sql/CreateTables.sql");

    @MockBean
    private IUserRepository userRepository;

    @MockBean
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private UserService userService;

    private UserEntity mockUser;
    private RoleEntity mockRole;

    @BeforeEach
    void setUp() {
        mockRole = new RoleEntityBuilder()
                .withRole(Role.MANAGER)
                .build();
        mockRole.setName(Role.MANAGER);

        mockUser = new UserEntityBuilder()
                .withUserName("TestUser")
                .withUserSecondName("TestSecondName")
                .withEmail("testuser@example.com")
                .withPasswordHash("password")
                .withRole(mockRole)
                .build();

        when(sequenceGeneratorService.generateSequence(any())).thenReturn(1L);
        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser);
    }

    @Test
    void testAddUser() {
        UserEntity savedUser = userService.addUser(mockUser);

        assertNotNull(savedUser);
        assertEquals("TestUser", savedUser.getUserName());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.of(mockUser));

        UserEntity foundUser = userService.getUserById(1);

        assertNotNull(foundUser);
        assertEquals("TestUser", foundUser.getUserName());
        verify(userRepository, times(1)).findById(anyInt());
    }
}
