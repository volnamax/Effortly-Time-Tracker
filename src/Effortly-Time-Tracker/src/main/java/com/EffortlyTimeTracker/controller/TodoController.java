package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.TodoListDTO;
import com.EffortlyTimeTracker.entity.TodoList;
import com.EffortlyTimeTracker.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "TODO-controller") // documentation
@RestController
@RequestMapping("api/todo")
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @Operation(summary = "Add todo")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public TodoList addTodo(@Valid @RequestBody TodoListDTO todoListDTO) {
        return todoService.addTodo(todoListDTO);
    }

    @Operation(summary = "Dell todo by id",
            description = "need id")
    @DeleteMapping("/del")
    public void delTodo(@RequestParam(required = true) Integer TodoId) {
        todoService.delTodoById(TodoId);
    }
    @Operation(summary = "Get all todo")
    @GetMapping("/get-all")
    public List<TodoList> getProjects() {
        return todoService.getAllTodo();
    }

}
