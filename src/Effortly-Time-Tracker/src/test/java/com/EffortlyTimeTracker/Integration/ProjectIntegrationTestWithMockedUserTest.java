package com.EffortlyTimeTracker.Integration;


import com.EffortlyTimeTracker.builder.ProjectEntityBuilder;
import com.EffortlyTimeTracker.builder.RoleEntityBuilder;
import com.EffortlyTimeTracker.builder.UserEntityBuilder;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.repository.postgres.RoleRepository;
import com.EffortlyTimeTracker.service.ProjectService;
import com.EffortlyTimeTracker.service.SequenceGeneratorService;
import com.EffortlyTimeTracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProjectIntegrationTestWithMockedUserTest {

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

    @Mock
    private SequenceGeneratorService sequenceGeneratorService;

    private UserEntity savedUser;
    private RoleEntity savedRole;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE project, user_app, roles CASCADE;");

        RoleEntity role = new RoleEntityBuilder()
                .withRole(Role.MANAGER)
                .build();
        savedRole = roleRepository.save(role);

        UserEntity user = new UserEntityBuilder()
                .withUserId(1)
                .withUserName("TestUser")
                .withUserSecondName("TestSecondName")
                .withEmail("testuser@example.com")
                .withPasswordHash("password123")
                .withRole(savedRole)
                .build();
        savedUser = userService.addUser(user);
    }

    @Test
    @Transactional
    void addProjectTest() {
        when(sequenceGeneratorService.generateSequence(anyString())).thenReturn(1L);

        ProjectEntity project = new ProjectEntityBuilder()
                .withProjectId(1)
                .withName("Test Project")
                .withUserProject(savedUser)
                .build();

        ProjectEntity savedProject = projectService.addProject(project);

        assertNotNull(savedProject, "Проект должен быть сохранен");
        assertEquals("Test Project", savedProject.getName(), "Название проекта должно совпадать");
        assertEquals(savedUser.getUserId(), savedProject.getUserProject().getUserId(), "Пользователь проекта должен совпадать с сохраненным пользователем");
    }

    @Test
    @Transactional
    void deleteProjectTest() {
        ProjectEntity project = new ProjectEntityBuilder()
                .withName("Test Project")
                .withUserProject(savedUser)
                .build();

        ProjectEntity savedProject = projectService.addProject(project);

        assertNotNull(projectService.getProjectsById(savedProject.getProjectId()), "Проект должен существовать перед удалением");

        projectService.delProjectById(savedProject.getProjectId());

        assertThrows(Exception.class, () -> projectService.getProjectsById(savedProject.getProjectId()), "Проект должен быть удален");
    }
}