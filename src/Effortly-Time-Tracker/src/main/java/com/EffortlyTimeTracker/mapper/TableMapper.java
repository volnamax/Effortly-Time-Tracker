package com.EffortlyTimeTracker.mapper;


import com.EffortlyTimeTracker.DTO.table.TableCreateDTO;
import com.EffortlyTimeTracker.DTO.table.TableDTO;
import com.EffortlyTimeTracker.DTO.table.TableResponseDTO;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.enums.Status;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import com.EffortlyTimeTracker.DTO.table.TableCreateDTO;
import com.EffortlyTimeTracker.DTO.table.TableResponseDTO;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.enums.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {ProjectMapper.class}) // Use ProjectMapper if needed for project entity mapping
public interface TableMapper {
    // Mapping from CreateDTO to Entity
    @Mapping(source = "projectId", target = "project.projectId")
    @Mapping(target = "tableId", ignore = true)
    @Mapping(source = "status", target = "status")
    TableEntity toEntity(TableCreateDTO dto);

    // Mapping from Entity to ResponseDTO
    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "tasks", target = "tasks")
    @Mapping(source = "tableId", target = "tableId")
    TableResponseDTO toResponseDTO(TableEntity entity);

    // Mapping from Entity to ResponseDTO
    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "tasks", target = "tasks")
    @Mapping(source = "tableId", target = "tableId")
    List<TableResponseDTO> toResponseDTO(List<TableEntity> entity);

    // Helper method to map Status enum from string
    default Status mapStatus(String status) {
        return Status.valueOf(status.toUpperCase());
    }

    // Helper method to convert List<TaskEntity> to List<Integer> (task IDs)
    default List<Integer> tasksToTaskIds(List<TaskEntity> tasks) {
        if (tasks == null) {
            return null;
        }
        return tasks.stream()
                .map(TaskEntity::getTaskId)
                .collect(Collectors.toList());
    }
}

//
//@Component
//public class TableMapper {
//
//    public TableEntity dtoToEntity(TableDTO tableDTO) {
//        TableEntity tableEntity = new TableEntity();
//        tableEntity.setName(tableDTO.getName());
//        tableEntity.setDescription(tableDTO.getDescription());
//        tableEntity.setStatus(Status.valueOf(tableDTO.getStatus()));
//        tableEntity.setProject(tableDTO.getProject());
//
//        return tableEntity;
//    }
//
//    public TableDTO entityToDto(TableEntity tableEntity) {
//        TableDTO tableDTO = new TableDTO();
//        tableDTO.setName(tableEntity.getName());
//        tableDTO.setDescription(tableEntity.getDescription());
//        tableDTO.setStatus(tableEntity.getStatus().name());
//        tableDTO.setProject(tableEntity.getProject());
//        return tableDTO;
//    }
//
//    public List<TableDTO> entityListToDtoList(List<TableEntity> entities) {
//        return entities.stream()
//                .map(this::entityToDto)
//                .collect(Collectors.toList());
//    }
//
//}
