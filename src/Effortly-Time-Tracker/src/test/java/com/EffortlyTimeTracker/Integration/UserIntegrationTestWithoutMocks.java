package com.EffortlyTimeTracker.Integration;

import com.EffortlyTimeTracker.builder.RoleEntityBuilder;
import com.EffortlyTimeTracker.builder.UserEntityBuilder;
import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.repository.postgres.RoleRepository;
import com.EffortlyTimeTracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserIntegrationTestWithoutMocks {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("sql/CreateTables.sql");

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void clearDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE user_app, roles CASCADE;");
    }

    @Test
    @Transactional
    public void testAddUser() {
        RoleEntity role = new RoleEntityBuilder()
                .withRole(Role.MANAGER)
                .build();

        RoleEntity savedRole = roleRepository.save(role);

        UserEntity user = new UserEntityBuilder()
                .withUserName("TestUser")
                .withUserSecondName("TestSecondName")
                .withEmail("testuser@example.com")
                .withPasswordHash("password123")
                .withRole(savedRole)
                .build();

        UserEntity savedUser = userService.addUser(user);

        assertNotNull(savedUser, "Сохраненный пользователь не должен быть null");
        assertNotNull(savedUser.getUserId(), "ID пользователя не должен быть null");
        assertEquals("TestUser", savedUser.getUserName(), "Имя пользователя должно совпадать");
        assertEquals("TestSecondName", savedUser.getUserSecondname(), "Фамилия пользователя должна совпадать");
        assertEquals("testuser@example.com", savedUser.getEmail(), "Email пользователя должен совпадать");
        assertEquals(Role.MANAGER, savedUser.getRole().getName(), "Роль пользователя должна быть MANAGER");
        Integer userIdInDb = jdbcTemplate.queryForObject(
                "SELECT user_id FROM user_app WHERE email = ?",
                new Object[]{savedUser.getEmail()},
                Integer.class
        );
        assertEquals(savedUser.getUserId(), userIdInDb, "ID пользователя в базе данных должен совпадать с сохраненным пользователем");
    }
}
