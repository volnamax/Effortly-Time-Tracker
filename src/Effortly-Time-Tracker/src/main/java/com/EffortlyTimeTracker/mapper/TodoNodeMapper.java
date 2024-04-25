package com.EffortlyTimeTracker.mapper;

import com.EffortlyTimeTracker.DTO.TodoNodeDTO;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.enums.Priority;
import com.EffortlyTimeTracker.enums.Status;
import org.springframework.stereotype.Component;


@Component
public class TodoNodeMapper {

    public TodoNodeEntity dtoToEntityCreate(TodoNodeDTO todoNodeDTO) {
        TodoNodeEntity todoNodeEntity = new TodoNodeEntity();

        todoNodeEntity.setContent(todoNodeDTO.getContent());
        todoNodeEntity.setStatus(Status.valueOf(todoNodeDTO.getStatus()));
        todoNodeEntity.setPriority(Priority.valueOf(todoNodeDTO.getPriority()));
        todoNodeEntity.setUser(todoNodeDTO.getUserTodo());
        todoNodeEntity.setDueData(todoNodeDTO.getDueData());

        return todoNodeEntity;
    }

    public TodoNodeDTO entityToDto(TodoNodeEntity todoNodeEntity) {
        TodoNodeDTO todoNodeDTO = new TodoNodeDTO();

        todoNodeDTO.setContent(todoNodeEntity.getContent());
        todoNodeDTO.setStatus(todoNodeEntity.getStatus().name());
        todoNodeDTO.setPriority(todoNodeEntity.getPriority().name());
        todoNodeDTO.setUserTodo(todoNodeEntity.getUser());
        todoNodeDTO.setDueData(todoNodeDTO.getDueData());

        return todoNodeDTO;
    }
}
