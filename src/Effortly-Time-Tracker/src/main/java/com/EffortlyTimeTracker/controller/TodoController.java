package com.EffortlyTimeTracker.controller;

//todo role check AOP

import com.EffortlyTimeTracker.DTO.todo.TodoNodeDTO;
import com.EffortlyTimeTracker.DTO.todo.TodoNodeResponseDTO;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.enums.Status;
import com.EffortlyTimeTracker.mapper.TodoNodeMapper;
import com.EffortlyTimeTracker.unit.TodoService;
import com.EffortlyTimeTracker.unit.middlewareOwn.todo.CheckOwner;
import com.EffortlyTimeTracker.unit.middlewareOwn.todo.CheckTaskOwner;
import com.EffortlyTimeTracker.unit.middlewareOwn.todo.CheckUserIdMatchesCurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Todo successfully added",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoNodeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions",
                    content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST')")
    @CheckUserIdMatchesCurrentUser
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
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST', 'ROLE_ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Todo successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Todo not found",
                    content = @Content)
    })
    @CheckTaskOwner
    public void delTodo(@RequestParam(required = true) Integer id) {
        log.info("Del todo: {}", id);
        todoService.delTodoById(id);
    }
    @Operation(summary = "Delete all todos by user id", description = "need user id")
    @DeleteMapping("/del-by-user-id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Todos successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "User not found or no todos to delete",
                    content = @Content)
    })
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST')")
    @CheckOwner
    public void delAllTodoBuUserID(@RequestParam(required = true) Integer id) {
        log.info("Deleting all todos for user with id: {}", id);

        // Проверяем, существуют ли задачи у пользователя
        List<TodoNodeEntity> todos = todoService.getAllTodoByIdUser(id);
        if (todos == null || todos.isEmpty()) {
            log.warn("No todos found for user with id: {}", id);
            throw new RuntimeException("User not found or no todos to delete");
        }

        // Если задачи есть, то удаляем их
        todoService.delAllTodoByIdUser(id);
        log.info("Successfully deleted all todos for user with id: {}", id);
    }

    @Operation(summary = "Get all todo by user id")
    @GetMapping("/get-all-by-user-id")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todos successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoNodeResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "User not found or Todo list is empty",
                    content = @Content)
    })
    @CheckOwner
    public List<TodoNodeResponseDTO> getTodoAll(@RequestParam Integer id) {
        log.info("Get todos by user id: {}", id);

        // Получаем задачи пользователя
        List<TodoNodeEntity> resTodoNodeEntity = todoService.getAllTodoByIdUser(id);

        // Проверяем, есть ли задачи у пользователя
        if (resTodoNodeEntity == null || resTodoNodeEntity.isEmpty()) {
            log.warn("No todos found for user with id: {}", id);
            throw new RuntimeException("User not found or Todo list is empty");
        }

        log.info("Response: {}", resTodoNodeEntity);
        List<TodoNodeResponseDTO> todoNodeResponseDTOS = todoNodeMapper.toDtoResponse(resTodoNodeEntity);
        log.info("Response: {}", todoNodeResponseDTOS);
        return todoNodeResponseDTOS;
    }

    @PatchMapping("/update-status")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todo status successfully updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoNodeResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Todo not found",
                    content = @Content)
    })
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST')")
    public ResponseEntity<?> updateTodoStatus(@RequestParam Integer id, @RequestParam Status status) {
        if (id == null || status == null) {
            log.error("Invalid request: id or status is null.");
            throw new IllegalArgumentException("ID and status cannot be null.");
        }

        log.info("Updating status of todo with ID: {} to {}", id, status);

        TodoNodeEntity updatedTodo = todoService.updateTodoStatus(id, status);  // Логика обновления статуса
        TodoNodeResponseDTO updatedTodoDTO = todoNodeMapper.toDtoResponse(updatedTodo);
        log.info("updatedTodoDTO {}", updatedTodoDTO);

        return ResponseEntity.ok(updatedTodoDTO);
    }


}
