//package com.EffortlyTimeTracker.mapper;
//
//import com.EffortlyTimeTracker.DTO.group.GroupDTO;
//import com.EffortlyTimeTracker.DTO.project.ProjectDTO;
//import com.EffortlyTimeTracker.entity.GroupEntity;
//import com.EffortlyTimeTracker.entity.ProjectEntity;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.Named;
//
//@Mapper(componentModel = "spring")
//public interface EntityMapper {
//    ProjectDTO projectToProjectDTO(ProjectEntity project);
//    ProjectEntity projectDTOToProject(ProjectDTO projectDTO);
//
//
//    GroupDTO groupToGroupDTO(GroupEntity group);
//
//    @Mapping(target = "projectId", source = "project.projectId")
//    GroupDTO groupEntityToGroupDTO(GroupEntity groupEntity);
//
//    @Mapping(target = "project", source = "projectId", qualifiedByName = "projectIdToProjectEntity")
//    GroupEntity groupDTOToGroup(GroupDTO groupDTO);
//
//
//    // Кастомный маппер для projectId -> ProjectEntity
//    @Named("projectIdToProjectEntity")
//    default ProjectEntity projectIdToProjectEntity(Integer projectId) {
//        if (projectId == null) {
//            return null;
//        }
//        ProjectEntity project = new ProjectEntity();
//        project.setProjectId(projectId);
//        return project;
//    }
//
//    // Использование кастомного маппинга в вашем методе
//    @Mapping(target = "project", source = "projectId", qualifiedByName = "projectIdToProjectEntity")
//        GroupEntity groupDTOToGroupWithProject(GroupDTO groupDTO);
//
//
////    //tagDTO
////    @Mapping(source = "project", target = "project")
////    TagDTO TagEntityToTagDto(TagEntity entity);
////
////    @Mapping(target = "tasks", ignore = true)
////    @Mapping(source = "project", target = "project")
////    TagEntity TagDtoToTagEntity(TagDTO dto);
////
////
////    //tagCREATEDTO
////    @Mapping(source = "project", target = "project")
////    TagCreateDTO TagEntityToTagCreateDto(TagEntity entity);
////
////    @Mapping(source = "project", target = "project")
////    TagEntity TagCreateDtoToTagEntity(TagCreateDTO dto);
//
//
//}