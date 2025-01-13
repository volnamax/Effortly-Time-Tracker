package com.EffortlyTimeTracker.integration;

import com.EffortlyTimeTracker.builder.ProjectEntityBuilder;
import com.EffortlyTimeTracker.builder.RoleEntityBuilder;
import com.EffortlyTimeTracker.builder.UserEntityBuilder;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.repository.postgres.RoleRepository;
import com.EffortlyTimeTracker.unit.ProjectService;
import com.EffortlyTimeTracker.unit.UserService;
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
public class ProjectWithoutMocksIntegrationTest {

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
    private ProjectService projectService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void clearDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE project, user_app, roles CASCADE;");
    }

    @Test
    @Transactional
    public void testAddProject() {
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

        ProjectEntity project = new ProjectEntityBuilder()
                .withName("Test Project")
                .withUserProject(savedUser)
                .build();

        ProjectEntity savedProject = projectService.addProject(project);

        assertNotNull(savedProject, "Сохраненный проект не должен быть null");
        assertNotNull(savedProject.getProjectId(), "ID проекта не должен быть null");
        assertEquals("Test Project", savedProject.getName(), "Название проекта должно совпадать");
        assertNotNull(savedProject.getUserProject(), "Проект должен быть связан с пользователем");
        assertEquals(savedUser.getUserId(), savedProject.getUserProject().getUserId(), "ID пользователя должен совпадать с пользователем, связанным с проектом");
    }
}
