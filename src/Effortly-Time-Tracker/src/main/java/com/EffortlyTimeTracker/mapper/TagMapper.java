package com.EffortlyTimeTracker.mapper;


import com.EffortlyTimeTracker.DTO.tag.TagCreateDTO;


import com.EffortlyTimeTracker.DTO.tag.TagDTO;
import com.EffortlyTimeTracker.DTO.tag.TagResponseDTO;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TagEntity;
import com.EffortlyTimeTracker.entity.TaskTagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TagMapper {
    @Mapping(source = "projectId", target = "project")
    TagEntity toEntity(TagCreateDTO dto);

    @Mapping(source = "project.projectId", target = "projectId")
    TagCreateDTO toDto(TagEntity entity);

    default ProjectEntity map(Integer projectId) {
        if (projectId == null) {
            return null;
        }
        ProjectEntity project = new ProjectEntity();
        project.setProjectId(projectId);
        return project;
    }
    default Integer getTaskIdFromTagDTO(TagCreateDTO tagDTO) {
        if (tagDTO == null) {
            return null;
        }
        return tagDTO.getTaskId();
    }


    @Mapping(target = "project", source = "project")
    @Mapping(source = "tasks", target = "tasks")
    TagDTO FullEntityToDto(TagEntity entity);

    @Mapping(source = "project", target = "project")
    @Mapping(target = "tasks", ignore = true)
    TagEntity FullDtoToEntity(TagDTO dto);

    List<TagDTO> FullEntityToDto(List<TagEntity> entities);


///response dto
// Response DTO mapping
@Mapping(source = "tagId", target = "projectID")
@Mapping(source = "tasks", target = "tasks", qualifiedByName = "mapTaskIds")
TagResponseDTO tagToTagResponseDTO(TagEntity tag);

    @Named("mapTaskIds")
    default Set<Integer> mapTaskIds(Set<TaskTagEntity> tasks) {
        return tasks != null ? tasks.stream()
                .map(taskTagEntity -> taskTagEntity.getTask().getTaskId())
                .collect(Collectors.toSet()) : null;
    }
}




//
//import com.EffortlyTimeTracker.DTO.table.TableDTO;
//import com.EffortlyTimeTracker.DTO.tag.TagDTO;
//import com.EffortlyTimeTracker.entity.ProjectEntity;
//import com.EffortlyTimeTracker.entity.TagEntity;
//import jdk.dynalink.linker.LinkerServices;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Component
//public class TagMapper {
//
//    public TagEntity dtoToEntity(TagDTO tagDTO) {
//        TagEntity tagEntity = new TagEntity();
//        tagEntity.setName(tagDTO.getName());
//        tagEntity.setColor(tagDTO.getColor());
//        tagEntity.setProject(tagDTO.getProject()); // Assumes project is properly managed and passed
//        // The tasks set is managed separately and should not be directly copied in a DTO to Entity mapping to avoid unintended side effects.
//        return tagEntity;
//    }
//
//    public TagDTO entityToDto(TagEntity tagEntity) {
//        TagDTO tagDTO = new TagDTO();
//        tagDTO.setName(tagEntity.getName());
//        tagDTO.setColor(tagEntity.getColor());
//        tagDTO.setProject(tagEntity.getProject()); // Assumes the project is fetched or appropriately handled
//        return tagDTO;
//    }
//    public Set<TagDTO> entitySetToDtoSet(Set<TagEntity> tagEntities) {
//        return tagEntities.stream()
//                .map(this::entityToDto)
//                .collect(Collectors.toSet());
//    }
//    public List<TagDTO> entitySetToDtoList(List<TagEntity> tagEntities) {
//        return tagEntities.stream()
//                .map(this::entityToDto)
//                .collect(Collectors.toList());
//    }
//}

