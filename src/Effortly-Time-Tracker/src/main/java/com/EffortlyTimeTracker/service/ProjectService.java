package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.DTO.ProjectDTO;
import com.EffortlyTimeTracker.entity.Project;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    public Project addProject(@NotNull ProjectDTO projectDTO) {
//        if (projectDTO.getName() == null || projectDTO.getName().isEmpty()) {
//            throw new InvalidProjectException("Имя проекта не может быть пустым");
//        }
        log.info("Добавление проекта: {}", projectDTO.getName());
        Project project = projectRepository.save(Project.builder()
                .name(projectDTO.getName())
                .description(projectDTO.getDescription())
                .userProject(projectDTO.getUserProject())
                .tables(projectDTO.getTables())
                .tags(projectDTO.getTags())
                .groupP(projectDTO.getGroupP())
                .build());
        log.info("Проект успешно добавлен: {}", project.getProjectId());
        return project;
    }

    public void delProjectById(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException(projectId);
        }
        projectRepository.deleteById(projectId);
        log.info("Project with id {} deleted", projectId);
    }

    public Project getProjectsById(Integer id) {
        Project proj = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        log.info("Get = " + proj);

        return proj;
    }

    public List<Project> getAllProject() {
        List<Project> projects = projectRepository.findAll();
        log.info("GetALL = " + projects);
        return projects;
    }

}
