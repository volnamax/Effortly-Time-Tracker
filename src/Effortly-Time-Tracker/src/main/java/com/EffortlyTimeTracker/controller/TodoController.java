package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.todoDTO.TodoNodeDTO;
import com.EffortlyTimeTracker.DTO.todoDTO.TodoNodeResponseDTO;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.mapper.TodoNodeMapper;
import com.EffortlyTimeTracker.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public void delTodo(@RequestParam(required = true) Integer TodoId) {
        log.info("Del todo: {}", TodoId);
        todoService.delTodoById(TodoId);
    }

    @Operation(summary = "Dell all todo by user id", description = "need user id")
    @DeleteMapping("/del-by-user-id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delAllTodoBuUserID(@RequestParam(required = true) Integer userId) {
        log.info("Del all todo by user: {}", userId);
        todoService.delAllTodoByIdUser(userId);
    }

    @Operation(summary = "Get all todo by id user")
    @GetMapping("/get-all-by-user-id")
    @ResponseStatus(HttpStatus.OK)
    public List<TodoNodeResponseDTO> getTodoAll(Integer id) {
        log.info("Get todo by id: {}", id);
        List<TodoNodeEntity> resTodoNodeEntity = todoService.getAllTodoByIdUser(id);
        log.info("Response: {}", resTodoNodeEntity);
        List<TodoNodeResponseDTO> todoNodeResponseDTOS = todoNodeMapper.toDtoResponse(resTodoNodeEntity);
        log.info("Response: {}", todoNodeResponseDTOS);
        return todoNodeResponseDTOS;
    }

    @Operation(summary = "Get all todo")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/get-all")
    public List<TodoNodeResponseDTO> getTodoAll() {
        List<TodoNodeEntity> resTodoNodeEntity = todoService.getAllTodo();
        log.info("Response: {}", resTodoNodeEntity);
        List<TodoNodeResponseDTO> todoNodeResponseDTOS = todoNodeMapper.toDtoResponse(resTodoNodeEntity);
        log.info("Response: {}", todoNodeResponseDTOS);
        return todoNodeResponseDTOS;

    }

}
