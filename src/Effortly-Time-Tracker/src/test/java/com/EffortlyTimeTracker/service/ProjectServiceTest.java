package com.EffortlyTimeTracker.service;



import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.exception.project.ProjectIsEmpty;
import com.EffortlyTimeTracker.repository.IProjectRepository;
import com.EffortlyTimeTracker.repository.IUserRepository;
import com.EffortlyTimeTracker.repository.postgres.ProjectPostgresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
        import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock
    private SequenceGeneratorService sequenceGeneratorService;  // Mock the SequenceGeneratorService


    @Mock
    private IProjectRepository projectPostgresRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    private ProjectEntity project;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setUserId(1);
        user.setUserName("TestUser");

        project = new ProjectEntity();
        project.setProjectId(1);
        project.setName("Test Project");
        project.setUserProject(user);
    }

    @Test
    void addProjectTest() {
        when(sequenceGeneratorService.generateSequence(anyString())).thenReturn(1L);  // Mock sequence generator

        when(projectPostgresRepository.save(any(ProjectEntity.class))).thenReturn(project);

        ProjectEntity savedProject = projectService.addProject(project);
        assertNotNull(savedProject);
        assertEquals("Test Project", savedProject.getName());

        verify(projectPostgresRepository).save(project);
    }

    @Test
    void deleteProjectByIdTestExists() {
        when(projectPostgresRepository.existsById(anyInt())).thenReturn(true);

        projectService.delProjectById(1);

        verify(projectPostgresRepository).deleteById(1);
    }

    @Test
    void deleteProjectByIdTestNotExists() {
        when(projectPostgresRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(ProjectNotFoundException.class, () -> projectService.delProjectById(1));
    }

    @Test
    void getProjectsByIdTestFound() {
        when(projectPostgresRepository.findById(anyInt())).thenReturn(Optional.of(project));

        ProjectEntity foundProject = projectService.getProjectsById(1);

        assertNotNull(foundProject);
        assertEquals("Test Project", foundProject.getName());
    }

    @Test
    void getProjectsByIdTestNotFound() {
        when(projectPostgresRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.getProjectsById(1));
    }

    @Test
    void getAllProjectsByIdUserTestFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(projectPostgresRepository.findByUserId(anyInt())).thenReturn(Arrays.asList(project));

        List<ProjectEntity> projects = projectService.getAllProjectByIdUser(1);
        assertFalse(projects.isEmpty());
        assertEquals(1, projects.size());
        assertEquals("Test Project", projects.get(0).getName());
    }

    @Test
    void getAllProjectsByIdUserTestNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(projectPostgresRepository.findByUserId(anyInt())).thenReturn(Arrays.asList());

        assertThrows(ProjectIsEmpty.class, () -> projectService.getAllProjectByIdUser(1));
    }
    @Test
    void deleteAllProjectsByIdUserTest() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(projectPostgresRepository.findByUserId(anyInt())).thenReturn(Arrays.asList(project));

        projectService.delAllProjectByIdUser(1);

        verify(projectPostgresRepository).deleteAll(anyList());
    }

    @Test
    void deleteAllProjectsByIdUserTestNoProjectsFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(projectPostgresRepository.findByUserId(anyInt())).thenReturn(Arrays.asList());

        projectService.delAllProjectByIdUser(1);

        verify(projectPostgresRepository, never()).deleteAll(anyList());
    }



}

