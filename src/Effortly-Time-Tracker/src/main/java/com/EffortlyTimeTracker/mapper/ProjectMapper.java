package com.EffortlyTimeTracker.mapper;


import com.EffortlyTimeTracker.DTO.ProjectDTO;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import org.springframework.stereotype.Component;

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
}
