package com.EffortlyTimeTracker.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.EffortlyTimeTracker.entity.Project;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.repository.ProjectRepository;
import com.EffortlyTimeTracker.DTO.ProjectDTO;
import com.EffortlyTimeTracker.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private ProjectDTO projectDTO;

    @BeforeEach
    void setUp() {
        projectDTO = new ProjectDTO();
        projectDTO.setName("Test Project");
    }

    @Test
    void addProject() {
        Project project = new Project();
        project.setName(projectDTO.getName());

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project savedProject = projectService.addProject(projectDTO);

        assertEquals(project.getName(), savedProject.getName());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void deleteProject_whenProjectExists() {
        Integer projectId = 1;
        when(projectRepository.existsById(projectId)).thenReturn(true);

        projectService.delProjectById(projectId);

        verify(projectRepository).deleteById(projectId);
    }

    @Test
    void deleteProject_whenProjectDoesNotExist_shouldThrowException() {
        Integer projectId = 1;
        when(projectRepository.existsById(projectId)).thenReturn(false);

        assertThrows(ProjectNotFoundException.class, () -> projectService.delProjectById(projectId));

        verify(projectRepository, never()).deleteById(any());


    }
    @Test
    void getProjectById_whenProjectExists() {
        Integer projectId = 1;
        Project expectedProject = new Project();
        expectedProject.setProjectId(projectId);
        expectedProject.setName("Test Project");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(expectedProject));

        Project actualProject = projectService.getProjectsById(projectId);

        assertEquals(expectedProject, actualProject);
    }

    @Test
    void getProjectById_whenProjectDoesNotExist_shouldThrowException() {
        Integer projectId = 1;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.getProjectsById(projectId));
    }
}
