package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.DTO.ProjectDTO;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    public ProjectEntity addProject(@NotNull ProjectEntity projectEntity) {
        log.info("Добавление проекта: ");
        ProjectEntity project = projectRepository.save(projectEntity);
        log.info("Проект успешно добавлен: {}", projectEntity);
        return project;
    }

    public void delProjectById(Integer id) {
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException(id);
        }
        projectRepository.deleteById(id);
        log.info("Project with id {} deleted", id);
    }

    public ProjectEntity getProjectsById(Integer id) {
        ProjectEntity proj = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        log.info("Get = " + proj);

        return proj;
    }


    @Transactional(readOnly = true)
    public List<ProjectDTO> getAllProject() {
        return projectRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ProjectDTO convertToDto(ProjectEntity projectEntity) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName(projectEntity.getName());
        projectDTO.setDescription(projectEntity.getDescription());
        projectDTO.setUserProject(projectEntity.getUserProject()); // Здесь может потребоваться преобразование UserEntity в UserDTO
        projectDTO.setGroupProject(projectEntity.getGroup()); // Аналогично, возможно потребуется преобразование GroupEntity в GroupDTO
        return projectDTO;
    }


}
