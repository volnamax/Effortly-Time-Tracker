package com.EffortlyTimeTracker.mapper;

import com.EffortlyTimeTracker.DTO.todo.TodoNodeDTO;
import com.EffortlyTimeTracker.DTO.todo.TodoNodeResponseDTO;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoNodeMapper {
    @Mapping(source = "user.userId", target = "userID")
    TodoNodeDTO toDto(TodoNodeEntity entity);

    @Mapping(source = "userID", target = "user.userId") // Reverse mapping for creating/updating entities
    @Mapping(target = "user", ignore = true)
    TodoNodeEntity toEntity(TodoNodeDTO dto);

    @Mapping(source = "userID", target = "user.userId") // Обратный маппинг для создания/обновления сущностей
    TodoNodeEntity toEntityResponse(TodoNodeResponseDTO dto);


    @Mapping(source = "user.userId", target = "userID")
    TodoNodeResponseDTO toDtoResponse(TodoNodeEntity entity);



    @Mapping(source = "user.userId", target = "userID")
    List<TodoNodeResponseDTO> toDtoResponse(List<TodoNodeEntity> entity);



}


//
//@Component
//public class TodoNodeMapper {
//
//    public TodoNodeEntity dtoToEntityCreate(TodoNodeDTO todoNodeDTO) {
//        TodoNodeEntity todoNodeEntity = new TodoNodeEntity();
//
//        todoNodeEntity.setContent(todoNodeDTO.getContent());
//        todoNodeEntity.setStatus(Status.valueOf(todoNodeDTO.getStatus()));
//        todoNodeEntity.setPriority(Priority.valueOf(todoNodeDTO.getPriority()));
//        todoNodeEntity.setUser(todoNodeDTO.getUserTodo());
//        todoNodeEntity.setDueData(todoNodeDTO.getDueData());
//
//        return todoNodeEntity;
//    }
//
//    public TodoNodeDTO entityToDto(TodoNodeEntity todoNodeEntity) {
//        TodoNodeDTO todoNodeDTO = new TodoNodeDTO();
//
//        todoNodeDTO.setContent(todoNodeEntity.getContent());
//        todoNodeDTO.setStatus(todoNodeEntity.getStatus().name());
//        todoNodeDTO.setPriority(todoNodeEntity.getPriority().name());
//        todoNodeDTO.setUserTodo(todoNodeEntity.getUser());
//        todoNodeDTO.setDueData(todoNodeDTO.getDueData());
//
//        return todoNodeDTO;
//    }
//
//    public List<TodoNodeDTO> entityListToDtoList(List<TodoNodeEntity> entities) {
//        return entities.stream()
//                .map(this::entityToDto)
//                .collect(Collectors.toList());
//    }
//}
