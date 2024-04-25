package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.TodoNodeDTO;
import com.EffortlyTimeTracker.DTO.UserCreateDTO;
import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.exception.todo.TodoNotFoudException;
import com.EffortlyTimeTracker.mapper.TodoNodeMapper;
import com.EffortlyTimeTracker.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<TodoNodeDTO> addTodo(@Valid @RequestBody TodoNodeDTO todoNodeDTO) {
        TodoNodeEntity todoNodeEntity =todoNodeMapper.dtoToEntityCreate(todoNodeDTO);
        TodoNodeEntity newTodoNode = todoService.addTodo(todoNodeEntity);
        TodoNodeDTO resTodoNodeDTO = todoNodeMapper.entityToDto(newTodoNode);
        return new ResponseEntity<>(resTodoNodeDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Dell todo by id", description = "need id")
    @DeleteMapping("/del")
    public void delTodo(@RequestParam(required = true) Integer TodoId) {
        todoService.delTodoById(TodoId);
    }

    @Operation(summary = "Dell all todo by user id", description = "need user id")
    @DeleteMapping("/del-by-user-id")
    public void delAllTodoBuUserID(@RequestParam(required = true) Integer userId) {
        todoService.delAllTodoByIdUser(userId);
    }

    @Operation(summary = "Get all todo by id user")
    @GetMapping("/get-all-by-User-Id")
    public List<TodoNodeEntity> getTodoAll(Integer id) {
        return todoService.getAllTodoByIdUser(id);
    }

    @Operation(summary = "Get all todo")
    @GetMapping("/get-all")
    public List<TodoNodeEntity> getTodoAll() {
        return todoService.getAllTodo();
    }

}
