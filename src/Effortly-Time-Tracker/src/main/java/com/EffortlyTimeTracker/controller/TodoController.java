package com.EffortlyTimeTracker.controller;

//todo role check

import com.EffortlyTimeTracker.DTO.todo.TodoNodeDTO;
import com.EffortlyTimeTracker.DTO.todo.TodoNodeResponseDTO;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.mapper.TodoNodeMapper;
import com.EffortlyTimeTracker.service.TodoService;
import com.EffortlyTimeTracker.service.middlewareOwn.CheckOwner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@Tag(name = "TODO-controller")
@RestController
@RequestMapping("api/todo")
public class TodoController {
    private final TodoService todoService;
    private final TodoNodeMapper todoNodeMapper;

    @Autowired
    public TodoController(TodoService todoService, TodoNodeMapper todoNodeMapper) {
        this.todoService = todoService;
        this.todoNodeMapper = todoNodeMapper;
    }

    @Operation(summary = "Add todo", description = "need: user, content, status: (NO_ACTIVE, ACTIVE), " +
            "priority(IMPORTANT_URGENTLY, NO_IMPORTANT_URGENTLY, IMPORTANT_NO_URGENTLY, NO_IMPORTANT_NO_URGENTLY)")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST')")
    public ResponseEntity<TodoNodeResponseDTO> addTodo(@Valid @RequestBody TodoNodeDTO todoNodeDTO) {
        TodoNodeEntity todoNodeEntity = todoNodeMapper.toEntity(todoNodeDTO);
        log.info("Add todo: {}", todoNodeEntity);

        TodoNodeEntity newTodoNode = todoService.addTodo(todoNodeEntity);
        log.info("New todo: {}", newTodoNode);
        TodoNodeResponseDTO resTodoNodeDTO = todoNodeMapper.toDtoResponse(newTodoNode);
        log.info("Response: {}", resTodoNodeDTO);

        return new ResponseEntity<>(resTodoNodeDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Dell todo by id", description = "need id")
    @DeleteMapping("/del")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST')")
    public void delTodo(@RequestParam(required = true) Integer id) {
        log.info("Del todo: {}", id);
        todoService.delTodoById(id);
    }

    @Operation(summary = "Dell all todo by user id", description = "need user id")
    @DeleteMapping("/del-by-user-id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST')")
    public void delAllTodoBuUserID(@RequestParam(required = true) Integer id) {
        log.info("Del all todo by user: {}", id);
        todoService.delAllTodoByIdUser(id);
    }

    @Operation(summary = "Get all todo by id user")
    @GetMapping("/get-all-by-user-id")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST')")
    @CheckOwner
    public List<TodoNodeResponseDTO> getTodoAll(Integer id) {
        log.info("Get todo by id: {}", id);
        List<TodoNodeEntity> resTodoNodeEntity = todoService.getAllTodoByIdUser(id);
        log.info("Response: {}", resTodoNodeEntity);
        List<TodoNodeResponseDTO> todoNodeResponseDTOS = todoNodeMapper.toDtoResponse(resTodoNodeEntity);
        log.info("Response: {}", todoNodeResponseDTOS);
        return todoNodeResponseDTOS;
    }

}
