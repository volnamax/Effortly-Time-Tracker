package com.EffortlyTimeTracker.service;


import com.EffortlyTimeTracker.DTO.project.ProjectAnalyticsDTO;
import com.EffortlyTimeTracker.builder.ProjectEntityBuilder;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.exception.project.ProjectIsEmpty;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.repository.IProjectRepository;
import com.EffortlyTimeTracker.repository.ITaskRepository;
import com.EffortlyTimeTracker.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock
    private SequenceGeneratorService sequenceGeneratorService;


    @Mock
    private IProjectRepository projectRepository;

    @Mock
    private IUserRepository userRepository;
    @Mock
    private ITaskRepository taskRepository;
    @InjectMocks
    private ProjectService projectService;

    private ProjectEntity project;
    private UserEntity user;
    private TaskEntity task;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setUserId(1);
        user.setUserName("TestUser");

        project = new ProjectEntityBuilder()
                .withProjectId(1)
                .withName("Test Project")
                .withUserProject(user)
                .build();

        task = new TaskEntity();
        task.setTaskId(1);
        task.setName("Test Task");
        task.setTimeAddTask(LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault()));
        task.setTimeEndTask(LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis() + 1000), ZoneId.systemDefault()));
        task.setSumTimer(1000L);
        task.setProjectId(project.getProjectId());

    }

    @Test
    void addProjectTest() {
        when(sequenceGeneratorService.generateSequence(anyString())).thenReturn(1L);  // Mock sequence generator

        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(project);

        ProjectEntity savedProject = projectService.addProject(project);
        assertNotNull(savedProject);
        assertEquals("Test Project", savedProject.getName());

        verify(projectRepository).save(project);
    }

    @Test
    void deleteProjectByIdTestExists() {
        when(projectRepository.existsById(anyInt())).thenReturn(true);

        projectService.delProjectById(1);

        verify(projectRepository).deleteById(1);
    }

    @Test
    void deleteProjectByIdTestNotExists() {
        when(projectRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(ProjectNotFoundException.class, () -> projectService.delProjectById(1));
    }

    @Test
    void getProjectsByIdTestFound() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));

        ProjectEntity foundProject = projectService.getProjectsById(1);

        assertNotNull(foundProject);
        assertEquals("Test Project", foundProject.getName());
    }

    @Test
    void getProjectsByIdTestNotFound() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.getProjectsById(1));
    }

    @Test
    void getAllProjectsByIdUserTestFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(projectRepository.findByUserId(anyInt())).thenReturn(Arrays.asList(project));

        List<ProjectEntity> projects = projectService.getAllProjectByIdUser(1);
        assertFalse(projects.isEmpty());
        assertEquals(1, projects.size());
        assertEquals("Test Project", projects.get(0).getName());
    }

    @Test
    void getAllProjectsByIdUserTestNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(projectRepository.findByUserId(anyInt())).thenReturn(Arrays.asList());

        assertThrows(ProjectIsEmpty.class, () -> projectService.getAllProjectByIdUser(1));
    }

    @Test
    void deleteAllProjectsByIdUserTest() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(projectRepository.findByUserId(anyInt())).thenReturn(Arrays.asList(project));

        projectService.delAllProjectByIdUser(1);

        verify(projectRepository).deleteAll(anyList());
    }

    @Test
    void deleteAllProjectsByIdUserTestNoProjectsFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(projectRepository.findByUserId(anyInt())).thenReturn(Arrays.asList());

        projectService.delAllProjectByIdUser(1);

        verify(projectRepository, never()).deleteAll(anyList());
    }

    @Test
    void getProjectAnalyticsTest() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(taskRepository.findByProjectId(anyInt())).thenReturn(Arrays.asList(task));

        ProjectAnalyticsDTO analytics = projectService.getProjectAnalytics(1);

        assertNotNull(analytics);
        assertEquals(1, analytics.getProjectId());
        assertEquals("Test Project", analytics.getProjectName());
        assertEquals(1, analytics.getTasks().size());
        assertEquals(1000L, analytics.getMaxTimeSpent());
    }

    @Test
    void getProjectAnalyticsTestNoTasks() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(taskRepository.findByProjectId(anyInt())).thenReturn(Collections.emptyList());

        ProjectAnalyticsDTO analytics = projectService.getProjectAnalytics(1);

        assertNotNull(analytics);
        assertTrue(analytics.getTasks().isEmpty());
        assertEquals(0L, analytics.getMaxTimeSpent());
        assertEquals(0L, analytics.getMinTimeSpent());
        assertEquals(0L, analytics.getAverageTimeSpent());
        assertEquals(0L, analytics.getMedianTimeSpent());
    }


    @Test
    void getProjectAnalyticsTestProjectNotFound() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.getProjectAnalytics(1));
    }
    @Test
    void getAllProjectsTest() {
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project));

        List<ProjectEntity> allProjects = projectService.getAllProject();

        assertNotNull(allProjects);
        assertEquals(1, allProjects.size());
        assertEquals("Test Project", allProjects.get(0).getName());

        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void getProjectAnalyticsTestWithMultipleTasks() {
        TaskEntity task2 = new TaskEntity();
        task2.setTaskId(2);
        task2.setName("Task 2");
        task2.setTimeAddTask(LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis() + 2000), ZoneId.systemDefault()));
        task2.setTimeEndTask(LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis() + 3000), ZoneId.systemDefault()));
        task2.setSumTimer(2000L);
        task2.setProjectId(project.getProjectId());

        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(taskRepository.findByProjectId(anyInt())).thenReturn(Arrays.asList(task, task2));

        ProjectAnalyticsDTO analytics = projectService.getProjectAnalytics(1);

        assertNotNull(analytics);
        assertEquals(2, analytics.getTasks().size());
        assertEquals(2000L, analytics.getMaxTimeSpent());  // Task 2 has the max time
        assertEquals(1000L, analytics.getMinTimeSpent());  // Task 1 has the min time
        assertEquals(1500L, analytics.getAverageTimeSpent());  // Average time is (1000 + 2000) / 2
        assertEquals(1500L, analytics.getMedianTimeSpent());  // Median for two tasks
    }


}

