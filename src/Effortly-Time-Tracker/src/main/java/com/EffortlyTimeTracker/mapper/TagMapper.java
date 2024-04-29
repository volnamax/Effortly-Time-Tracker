package com.EffortlyTimeTracker.mapper;


import com.EffortlyTimeTracker.DTO.TableDTO;
import com.EffortlyTimeTracker.DTO.TagDTO;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TagEntity;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TagMapper {

    public TagEntity dtoToEntity(TagDTO tagDTO) {
        TagEntity tagEntity = new TagEntity();
        tagEntity.setName(tagDTO.getName());
        tagEntity.setColor(tagDTO.getColor());
        tagEntity.setProject(tagDTO.getProject()); // Assumes project is properly managed and passed
        // The tasks set is managed separately and should not be directly copied in a DTO to Entity mapping to avoid unintended side effects.
        return tagEntity;
    }

    public TagDTO entityToDto(TagEntity tagEntity) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName(tagEntity.getName());
        tagDTO.setColor(tagEntity.getColor());
        tagDTO.setProject(tagEntity.getProject()); // Assumes the project is fetched or appropriately handled
        return tagDTO;
    }
    public Set<TagDTO> entitySetToDtoSet(Set<TagEntity> tagEntities) {
        return tagEntities.stream()
                .map(this::entityToDto)
                .collect(Collectors.toSet());
    }
    public List<TagDTO> entitySetToDtoList(List<TagEntity> tagEntities) {
        return tagEntities.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}

