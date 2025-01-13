package com.EffortlyTimeTracker.controller.v2;

//todo role check AOP

import com.EffortlyTimeTracker.DTO.todo.TodoNodeDTO;
import com.EffortlyTimeTracker.DTO.todo.TodoNodeResponseDTO;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.enums.Status;
import com.EffortlyTimeTracker.mapper.TodoNodeMapper;
import com.EffortlyTimeTracker.unit.TodoService;
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

@Slf4j
@Tag(name = "TODO-V2-controller")
@RestController
@RequestMapping("api/v2/todos")
public class TodoControllerV2 {
    private final TodoService todoService;
    private final TodoNodeMapper todoNodeMapper;

    @Autowired
    public TodoControllerV2(TodoService todoService, TodoNodeMapper todoNodeMapper) {
        this.todoService = todoService;
        this.todoNodeMapper = todoNodeMapper;
    }

    @Operation(summary = "Add todo", description = "need: user, content, status: (NO_ACTIVE, ACTIVE), " +
            "priority(IMPORTANT_URGENTLY, NO_IMPORTANT_URGENTLY, IMPORTANT_NO_URGENTLY, NO_IMPORTANT_NO_URGENTLY)")
    @PostMapping()
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

    @Operation(summary = "Delete todo by id", description = "Deletes todo by given id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST', 'ROLE_ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Todo successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Todo not found", content = @Content)
    })
    @CheckTaskOwner
    public void delTodo(@PathVariable("id") Integer id) {
        log.info("Delete todo: {}", id);
        todoService.delTodoById(id);
    }


    @PatchMapping("/{todoId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update Todo status by ID",
            description = "Updates the status of a Todo item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todo status successfully updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoNodeResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Todo not found",
                    content = @Content)
    })
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST')")
    public ResponseEntity<?> updateTodoStatus(@PathVariable("todoId") Integer id, @RequestBody Status status) {
        if (id == null || status == null) {
            log.error("Invalid request: id or status is null.");
            throw new IllegalArgumentException("ID and status cannot be null.");
        }

        log.info("Updating status of todo with ID: {} to {}", id, status);

        // Логика обновления статуса
        TodoNodeEntity updatedTodo = todoService.updateTodoStatus(id, status);
        TodoNodeResponseDTO updatedTodoDTO = todoNodeMapper.toDtoResponse(updatedTodo);
        log.info("Updated TodoDTO: {}", updatedTodoDTO);

        return ResponseEntity.ok(updatedTodoDTO);
    }


}
