package com.EffortlyTimeTracker.service;
import com.EffortlyTimeTracker.exception.InvalidProjectException;

import com.EffortlyTimeTracker.DTO.ProjectDTO;
import com.EffortlyTimeTracker.entity.*;
import com.EffortlyTimeTracker.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public Project addProject(ProjectDTO projectDTO) {
        // Пример проверки для демонстрации выброса исключения
        if (projectDTO.getName() == null || projectDTO.getName().isEmpty()) {
            throw new InvalidProjectException("Имя проекта не может быть пустым");
        }

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
    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    // Добавьте другие методы, которые вам нужны
}
