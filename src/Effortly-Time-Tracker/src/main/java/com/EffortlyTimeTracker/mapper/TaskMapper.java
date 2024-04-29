package com.EffortlyTimeTracker.mapper;

import com.EffortlyTimeTracker.DTO.TableDTO;
import com.EffortlyTimeTracker.DTO.TaskDTO;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.enums.Status;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public TaskEntity dtoToEntity(TaskDTO taskDTO) {
        TaskEntity taskEntity = new TaskEntity();

        taskEntity.setName(taskDTO.getName());
        taskEntity.setDescription(taskDTO.getDescription());
        taskEntity.setStatus(com.EffortlyTimeTracker.enums.Status.valueOf(taskDTO.getStatus()));
        taskEntity.setSumTimer(taskDTO.getSumTimer());
        taskEntity.setStartTimer(taskDTO.getStartTimer());
        taskEntity.setTimeAddTask(taskDTO.getTimeAddTask());
        taskEntity.setTimeEndTask(taskDTO.getTimeEndTask());
        taskEntity.setTable(taskDTO.getTable()); // Assumes table is properly managed and passed

        return taskEntity;
    }

    public TaskDTO entityToDto(TaskEntity taskEntity) {
        TaskDTO taskDTO = new TaskDTO();

        taskDTO.setName(taskEntity.getName());
        taskDTO.setDescription(taskEntity.getDescription());
        taskDTO.setStatus(taskEntity.getStatus().toString());
        taskDTO.setSumTimer(taskEntity.getSumTimer());
        taskDTO.setStartTimer(taskEntity.getStartTimer());
        taskDTO.setTimeAddTask(taskEntity.getTimeAddTask());
        taskDTO.setTimeEndTask(taskEntity.getTimeEndTask());
        taskDTO.setTable(taskEntity.getTable()); // Assumes the table is fetched or appropriately handled

        return taskDTO;
    }

    public List<TaskDTO> entityListToDtoList(List<TaskEntity> entities) {
        return entities.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

//    public Set<TaskDTO> entitySetToDtoSet(Set<TaskEntity> taskEntities) {
//        return taskEntities.stream()
//                .map(this::entityToDto)
//                .collect(Collectors.toSet());
//    }
}
