package com.EffortlyTimeTracker.mapper;

import com.EffortlyTimeTracker.DTO.task.TaskCreateDTO;
import com.EffortlyTimeTracker.DTO.task.TaskResponseDTO;
import com.EffortlyTimeTracker.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    // Mapping from CreateDTO to Entity
    @Mappings({
            @Mapping(source = "tableId", target = "table.tableId"),
            @Mapping(target = "taskId", ignore = true), // Task ID is auto-generated
            @Mapping(target = "tasks", ignore = true) // Ignore 'tasks' field from TaskEntity
    })
    TaskEntity toEntity(TaskCreateDTO dto);

    // Mapping from Entity to ResponseDTO
    @Mappings({
            @Mapping(source = "table.tableId", target = "tableId"),
            @Mapping(target = "tags", ignore = true) // If handling of tags is needed, define appropriate mapping
    })
    TaskResponseDTO toResponseDTO(TaskEntity entity);

    // Method to map lists of entities to lists of DTOs
    default List<TaskResponseDTO> toResponseDTO(List<TaskEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

//
//    public TaskEntity dtoToEntity(TaskDTO taskDTO) {
//        TaskEntity taskEntity = new TaskEntity();
//
//        taskEntity.setName(taskDTO.getName());
//        taskEntity.setDescription(taskDTO.getDescription());
//        taskEntity.setStatus(com.EffortlyTimeTracker.enums.Status.valueOf(taskDTO.getStatus()));
//        taskEntity.setSumTimer(taskDTO.getSumTimer());
//        taskEntity.setStartTimer(taskDTO.getStartTimer());
//        taskEntity.setTimeAddTask(taskDTO.getTimeAddTask());
//        taskEntity.setTimeEndTask(taskDTO.getTimeEndTask());
//        taskEntity.setTable(taskDTO.getTable()); // Assumes table is properly managed and passed
//
//        return taskEntity;
//    }
//
//    public TaskDTO entityToDto(TaskEntity taskEntity) {
//        TaskDTO taskDTO = new TaskDTO();
//
//        taskDTO.setName(taskEntity.getName());
//        taskDTO.setDescription(taskEntity.getDescription());
//        taskDTO.setStatus(taskEntity.getStatus().toString());
//        taskDTO.setSumTimer(taskEntity.getSumTimer());
//        taskDTO.setStartTimer(taskEntity.getStartTimer());
//        taskDTO.setTimeAddTask(taskEntity.getTimeAddTask());
//        taskDTO.setTimeEndTask(taskEntity.getTimeEndTask());
//        taskDTO.setTable(taskEntity.getTable()); // Assumes the table is fetched or appropriately handled
//
//        return taskDTO;
//    }
//
//    public List<TaskDTO> entityListToDtoList(List<TaskEntity> entities) {
//        return entities.stream()
//                .map(this::entityToDto)
//                .collect(Collectors.toList());
//    }

//    public Set<TaskDTO> entitySetToDtoSet(Set<TaskEntity> taskEntities) {
//        return taskEntities.stream()
//                .map(this::entityToDto)
//                .collect(Collectors.toSet());
//    }
}
