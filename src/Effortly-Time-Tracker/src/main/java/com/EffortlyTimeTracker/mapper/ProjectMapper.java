package com.EffortlyTimeTracker.mapper;


import com.EffortlyTimeTracker.DTO.ProjectDTO;
import com.EffortlyTimeTracker.DTO.TodoNodeDTO;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    public ProjectEntity dtoToEntity(ProjectDTO projectDTO) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setName(projectDTO.getName());
        projectEntity.setDescription(projectDTO.getDescription());
        projectEntity.setUserProject(projectDTO.getUserProject());
        projectEntity.setGroup(projectDTO.getGroupProject());
        return projectEntity;
    }

    public ProjectDTO entityToDto(ProjectEntity projectEntity) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName(projectEntity.getName());
        projectDTO.setDescription(projectEntity.getDescription());
        projectDTO.setUserProject(projectEntity.getUserProject());
        projectDTO.setGroupProject(projectEntity.getGroup());
        return projectDTO;
    }

    public List<ProjectDTO> entityListToDtoList(List<ProjectEntity> entities) {
        return entities.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}
