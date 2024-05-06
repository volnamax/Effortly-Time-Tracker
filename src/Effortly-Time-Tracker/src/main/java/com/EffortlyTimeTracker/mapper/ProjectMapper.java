package com.EffortlyTimeTracker.mapper;


import com.EffortlyTimeTracker.DTO.project.ProjectCreateDTO;
import com.EffortlyTimeTracker.DTO.project.ProjectDTO;
import com.EffortlyTimeTracker.DTO.project.ProjectResponseDTO;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.TagEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {java.util.stream.Collectors.class})
public interface ProjectMapper {


    // Mapping from CreateDTO to Entity
    @Mapping(target = "projectId", ignore = true) // Ignore as it's auto-generated
    @Mapping(source = "userProject", target = "userProject.userId") // Assuming userProject in DTO is an ID and mapped to userProject entity in ProjectEntity
    @Mapping(target = "group", ignore = true) // Ignore if no group data is present in DTO
    @Mapping(target = "tables", ignore = true) // Assume not setting tables on creation
    @Mapping(target = "tags", ignore = true) // Assume not setting tags on creation
    ProjectEntity toEntity(ProjectCreateDTO dto);

    // Mapping from Entity to ResponseDTO
    @Mapping(source = "projectId", target = "id")
    @Mapping(source = "userProject.userId", target = "userProject") // Flatten user entity to user ID
    @Mapping(source = "tables", target = "tablesId") // Assume you have method to extract IDs from tables
    @Mapping(source = "tags", target = "tagsId") // Assume you have method to extract IDs from tags
    ProjectResponseDTO toResponseDTO(ProjectEntity entity);

    // Helper methods to extract IDs from collections
    default Integer tableToTableId(TableEntity table) {
        return table == null ? null : table.getTableId();
    }

    default Integer tagToTagId(TagEntity tag) {
        return tag == null ? null : tag.getTagId();
    }

//    ProjectEntity toEntity(ProjectCreateDTO dto);
//
//    ProjectResponseDTO toDtoResponse(ProjectEntity entity);
//
//    default Integer mapUserProject(UserEntity user) {
//        return user == null ? null : user.getUserId();
//    }
//
//    default UserEntity mapUserIdToUserEntity(Integer userId) {
//        // Здесь должен быть вызов к сервису загрузки UserEntity по ID
//        // Возвращение null для простоты, замените на реальный вызов к базе данных
//        return new UserEntity(userId);
//    }
//    default UserEntity userIdToUserEntity(Integer userId) {
//        return new UserEntity(userId);
//    }


    //    @Mapping(source = "userProject", target = "userProject.userId")
//    ProjectCreateDTO toDto(ProjectEntity entity);
//    @Mapping(target = "userProject", source = "userProject.userId")
//    ProjectDTO toDto(ProjectEntity entity);
//
//    @Mapping(target = "userProject", source = "userProject.userId")
//    ProjectEntity toEntity(ProjectDTO dto);

//    @Mapping(target = "id", source = "projectId")
//    @Mapping(target = "userProject", expression = "java(userIdToUserEntity(entity.getUserProject().getUserId()))")
//    @Mapping(target = "tablesId", expression = "java(entity.getTables().stream().map(TableEntity::getTableId).collect(Collectors.toList()))")
//    @Mapping(target = "tagsId", expression = "java(entity.getTags().stream().map(TagEntity::getTagId).collect(Collectors.toSet()))")
    //    ProjectResponseDTO toDtoResponse(ProjectEntity entity);


//    @Mapping(target = "id", source = "projectId")
//    @Mapping(target = "userProject", expression = "java(userIdToUserEntity(entity.getUserProject().getUserId()))")
//    @Mapping(target = "tablesId", expression = "java(mapTableIds(entity.getTables()))")
//    @Mapping(target = "tagsId", expression = "java(mapTagIds(entity.getTags()))")
//    ProjectResponseDTO toDtoResponse(ProjectEntity entity);

    // Кастомные методы для маппинга коллекций
    default List<Integer> mapTableIds(List<TableEntity> tables) {
        if (tables == null) {
            return null;
        }
        return tables.stream().map(TableEntity::getTableId).collect(Collectors.toList());
    }

    default Set<Integer> mapTagIds(Set<TagEntity> tags) {
        if (tags == null) {
            return null;
        }
        return tags.stream().map(TagEntity::getTagId).collect(Collectors.toSet());
    }
//
//    // Пример метода для преобразования userId в UserEntity
//    @Named("userIdToUserEntity")
//    default UserEntity userIdToUserEntity(Integer userId) {
//        // Здесь должен быть ваш сервис или логика поиска UserEntity по userId
//        return new UserEntity(userId); // Пример создания UserEntity только с userId
//    }

}
//import com.EffortlyTimeTracker.DTO.project.ProjectDTO;
//import com.EffortlyTimeTracker.entity.ProjectEntity;ву
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class ProjectMapper {
//
//    public ProjectEntity dtoToEntity(ProjectDTO projectDTO) {
//        ProjectEntity projectEntity = new ProjectEntity();
//        projectEntity.setName(projectDTO.getName());
//        projectEntity.setDescription(projectDTO.getDescription());
//        projectEntity.setUserProject(projectDTO.getUserProject());
////        if (projectDTO.getGroupId() != null) {
////            GroupEntity groupEntity = new GroupEntity();
////            groupEntity.setGroupId(projectDTO.getGroupId());
////            projectEntity.setGroup(groupEntity);
////        }
//
//        return projectEntity;
//    }
//
//    public ProjectDTO entityToDto(ProjectEntity projectEntity) {
//        ProjectDTO projectDTO = new ProjectDTO();
//        projectDTO.setName(projectEntity.getName());
//        projectDTO.setDescription(projectEntity.getDescription());
//        projectDTO.setUserProject(projectEntity.getUserProject());
//
////        if (projectEntity.getGroup() != null)
////            projectDTO.setGroupId(projectEntity.getGroup().getGroupId());
//        return projectDTO;
//    }
//
//    public List<ProjectDTO> entityListToDtoList(List<ProjectEntity> entities) {
//        return entities.stream()
//                .map(this::entityToDto)
//                .collect(Collectors.toList());
//    }
//}



