package com.EffortlyTimeTracker.mapper;


import com.EffortlyTimeTracker.DTO.ProjectDTO;
import com.EffortlyTimeTracker.entity.GroupEntity;
import com.EffortlyTimeTracker.entity.ProjectEntity;
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
//        if (projectDTO.getGroupId() != null) {
//            GroupEntity groupEntity = new GroupEntity();
//            groupEntity.setGroupId(projectDTO.getGroupId());
//            projectEntity.setGroup(groupEntity);
//        }

        return projectEntity;
    }

    public ProjectDTO entityToDto(ProjectEntity projectEntity) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName(projectEntity.getName());
        projectDTO.setDescription(projectEntity.getDescription());
        projectDTO.setUserProject(projectEntity.getUserProject());

//        if (projectEntity.getGroup() != null)
//            projectDTO.setGroupId(projectEntity.getGroup().getGroupId());
        return projectDTO;
    }

    public List<ProjectDTO> entityListToDtoList(List<ProjectEntity> entities) {
        return entities.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}
