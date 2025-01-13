package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.task.InactiveTaskDTO;
import com.EffortlyTimeTracker.DTO.task.TaskCreateDTO;
import com.EffortlyTimeTracker.DTO.task.TaskResponseDTO;
import com.EffortlyTimeTracker.entity.InactiveTaskEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.mapper.TaskMapper;
import com.EffortlyTimeTracker.unit.TaskService;
import com.EffortlyTimeTracker.unit.middlewareOwn.table.CheckTableOwner;
import com.EffortlyTimeTracker.unit.middlewareOwn.task.CheckTaskOwner;
import com.EffortlyTimeTracker.unit.middlewareOwn.task.CheckUserIdMatchesCurrentUserTask;
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
@Tag(name = "Task-controller")
@RestController
@RequestMapping("api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @Operation(summary = "Add task", description = "need:  name and id table, status = ACTIVE NO_ACTIVEl")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Table not found", content = @Content)
    })
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_GUEST')")
    @CheckUserIdMatchesCurrentUserTask
    public ResponseEntity<TaskResponseDTO> addTask(@Valid @RequestBody TaskCreateDTO taskDTO) {
        log.info("api/task/add");
        log.info("taskDTO: {}", taskDTO);
        
        TaskEntity taskEntity = taskMapper.toEntity(taskDTO);
        TaskEntity task = taskService.addTask(taskEntity);

        TaskResponseDTO response = taskMapper.toResponseDTO(task);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Dell task by id",
            description = "need id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @DeleteMapping("/del")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckTaskOwner
    public void delTask(@RequestParam(required = true) Integer taskId) {
        log.info("api/task/del");
        log.info("taskId: {}", taskId);

        if (taskService.getTaskById(taskId) == null) {
            log.warn("Task with id {} not found", taskId);
            throw new RuntimeException("Task not found");
        }

        taskService.delTaskById(taskId);
    }

    @Operation(summary = "Get task by id",
            description = "need id")
    @GetMapping("/get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @CheckTaskOwner
    public TaskResponseDTO getTask(@RequestParam(required = true) Integer taskId) {
        log.info("api/task/get");
        log.info("taskId: {}", taskId);

        TaskEntity task = taskService.getTaskById(taskId);
        if (task == null) {
            log.warn("Task with id {} not found", taskId);
            throw new RuntimeException("Task not found");
        }

        return taskMapper.toResponseDTO(task);
    }

    @Operation(summary = "Get all task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content)
    })
    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<TaskResponseDTO> getTask() {
        log.info("api/task/get-all");
        List<TaskEntity> tasks = taskService.getAllTask();
        log.info("tasks: {}", tasks);

        List<TaskResponseDTO> taskResponseDTOS = taskMapper.toResponseDTO(tasks);
        log.info("taskResponseDTOS: {}", taskResponseDTOS);

        return taskResponseDTOS;
    }

    @Operation(summary = "Get all task by id table", description = "need table id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Table not found", content = @Content)
    })
    @GetMapping("/get-all-by-table-id")
    @ResponseStatus(HttpStatus.OK)
    @CheckTableOwner
    public List<TaskResponseDTO> getTaskAllByTableId(Integer id) {
        log.info("api/task/get-all-by-table-id");
        log.info("id: {}", id);

        if (taskService.getAllTaskByIdTable(id) == null) {
            log.warn("Table with id {} not found", id);
            throw new RuntimeException("Table not found");
        }

        List<TaskEntity> resEntity = taskService.getAllTaskByIdTable(id);
        return taskMapper.toResponseDTO(resEntity);
    }

    @Operation(summary = "Dell all task by table id", description = "need table id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tasks successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Table not found", content = @Content)
    })
    @DeleteMapping("/del-by-table-id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckTableOwner
    public void delAllTaskByTaskId(@RequestParam(required = true) Integer id) {
        log.info("api/task/del-by-table-id");
        log.info("id: {}", id);

        if (taskService.getAllTaskByIdTable(id) == null) {
            log.warn("Table with id {} not found", id);
            throw new RuntimeException("Table not found");
        }

        taskService.delAllTaskByIdTable(id);
    }

    @Operation(summary = "Start timer", description = "need task id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Timer successfully started",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @PostMapping("/start-timer")
    @ResponseStatus(HttpStatus.OK)
    @CheckTaskOwner
    public TaskResponseDTO startTaskTimer(@RequestParam Integer taskId) {
        log.info("api/task/start-timer");
        log.info("taskId: {}", taskId);

        // Проверка существования задачи
        TaskEntity task = taskService.getTaskById(taskId);
        if (task == null) {
            log.warn("Task with id {} not found", taskId);
            throw new RuntimeException("Task not found");
        }

        task = taskService.startTaskTimer(taskId);
        return taskMapper.toResponseDTO(task);
    }

    @Operation(summary = "stop timer", description = "need task id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Timer successfully stopped",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @PostMapping("/stop-timer")
    @ResponseStatus(HttpStatus.OK)
    @CheckTaskOwner
    public TaskResponseDTO stopTaskTimer(@RequestParam Integer taskId) {
        log.info("api/task/stop-timer");
        log.info("taskId: {}", taskId);

        // Проверка существования задачи
        TaskEntity task = taskService.getTaskById(taskId);
        if (task == null) {
            log.warn("Task with id {} not found", taskId);
            throw new RuntimeException("Task not found");
        }

        task = taskService.stopTaskTimer(taskId);
        return taskMapper.toResponseDTO(task);
    }

    @Operation(summary = "Complete timer", description = "need task id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully completed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @PostMapping("/complete")
    @ResponseStatus(HttpStatus.OK)
    @CheckTaskOwner
    public TaskResponseDTO completeTask(@RequestParam Integer taskId) {
        log.info("api/task/complete");
        log.info("taskId: {}", taskId);

        // Проверка существования задачи
        TaskEntity task = taskService.getTaskById(taskId);
        if (task == null) {
            log.warn("Task with id {} not found", taskId);
            throw new RuntimeException("Task not found");
        }

        task = taskService.completeTask(taskId);
        return taskMapper.toResponseDTO(task);
    }

    @Operation(summary = "Deactivate task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully deactivated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InactiveTaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @PostMapping("/deactivate")
    public ResponseEntity<InactiveTaskDTO> deactivateTask(@RequestParam Integer taskId, @RequestParam(required = false) String reason) {
        log.info("api/task/deactivate");
        log.info("taskId: {}", taskId);

        // Проверка существования задачи
        TaskEntity task = taskService.getTaskById(taskId);
        if (task == null) {
            log.warn("Task with id {} not found", taskId);
            throw new RuntimeException("Task not found");
        }

        InactiveTaskEntity inactiveTask = taskService.deactivateTask(taskId, reason);
        InactiveTaskDTO response = new InactiveTaskDTO(inactiveTask.getId(), inactiveTask.getTask().getTaskId(), inactiveTask.getDeactivatedAt(), inactiveTask.getReason());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
