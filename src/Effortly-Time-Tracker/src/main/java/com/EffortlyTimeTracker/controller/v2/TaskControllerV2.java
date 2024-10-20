package com.EffortlyTimeTracker.controller.v2;

import com.EffortlyTimeTracker.DTO.task.InactiveTaskDTO;
import com.EffortlyTimeTracker.DTO.task.TaskActionRequestDTO;
import com.EffortlyTimeTracker.DTO.task.TaskCreateDTO;
import com.EffortlyTimeTracker.DTO.task.TaskResponseDTO;
import com.EffortlyTimeTracker.entity.InactiveTaskEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.mapper.TaskMapper;
import com.EffortlyTimeTracker.service.TaskService;
import com.EffortlyTimeTracker.service.middlewareOwn.table.CheckTableOwner;
import com.EffortlyTimeTracker.service.middlewareOwn.task.CheckTaskOwner;
import com.EffortlyTimeTracker.service.middlewareOwn.task.CheckUserIdMatchesCurrentUserTask;
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
import java.util.Map;

@Slf4j
@Tag(name = "Task-V2-controller")
@RestController
@RequestMapping("api/v2/tasks")
public class TaskControllerV2 {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskControllerV2(TaskService taskService, TaskMapper taskMapper) {
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
    @PostMapping()
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


    @Operation(summary = "Delete task by id", description = "need id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckTaskOwner
    public void delTask(@PathVariable Integer taskId) {
        log.info("api/task/del");
        log.info("taskId: {}", taskId);

        if (taskService.getTaskById(taskId) == null) {
            log.warn("Task with id {} not found", taskId);
            throw new RuntimeException("Task not found");
        }

        taskService.delTaskById(taskId);
    }



    @Operation(summary = "Get task by id", description = "need id")
    @GetMapping("/{taskId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @CheckTaskOwner
    public TaskResponseDTO getTask(@PathVariable Integer taskId) {
        log.info("api/task/get");
        log.info("taskId: {}", taskId);

        TaskEntity task = taskService.getTaskById(taskId);
        if (task == null) {
            log.warn("Task with id {} not found", taskId);
            throw new RuntimeException("Task not found");
        }

        return taskMapper.toResponseDTO(task);
    }
    @Operation(summary = "Retrieve all tasks", description = "Get all tasks from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content)
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<TaskResponseDTO> getAllTasks() {
        log.info("api/task/get-all");
        List<TaskEntity> tasks = taskService.getAllTask();
        log.info("tasks: {}", tasks);

        List<TaskResponseDTO> taskResponseDTOS = taskMapper.toResponseDTO(tasks);
        log.info("taskResponseDTOS: {}", taskResponseDTOS);

        return taskResponseDTOS;
    }


    @Operation(summary = "Manage task timer", description = "Start, stop, or complete task timer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Action successfully performed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @PatchMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @CheckTaskOwner
    public TaskResponseDTO manageTaskTimer(
            @PathVariable Integer taskId,  // taskId теперь передаётся в URL
            @RequestBody TaskActionRequestDTO taskActionRequest // action передаётся через тело запроса как DTO
    ) {
        log.info("api/task/manage-timer");
        log.info("taskId: {}, action: {}", taskId, taskActionRequest.getAction());

        // Проверка существования задачи
        TaskEntity task = taskService.getTaskById(taskId);
        if (task == null) {
            log.warn("Task with id {} not found", taskId);
            throw new RuntimeException("Task not found");
        }

        // Получаем действие из тела запроса
        String action = taskActionRequest.getAction();
        if (action == null) {
            log.error("Action is not provided");
            throw new IllegalArgumentException("Action must be provided in the request body.");
        }

        // Определение действия на основе параметра "action"
        switch (action.toLowerCase()) {
            case "start":
                task = taskService.startTaskTimer(taskId);
                log.info("Timer started for taskId: {}", taskId);
                break;
            case "stop":
                task = taskService.stopTaskTimer(taskId);
                log.info("Timer stopped for taskId: {}", taskId);
                break;
            case "complete":
                task = taskService.completeTask(taskId);
                log.info("Task completed for taskId: {}", taskId);
                break;
            default:
                log.error("Invalid action: {}", action);
                throw new IllegalArgumentException("Invalid action. Allowed actions are: start, stop, complete.");
        }

        return taskMapper.toResponseDTO(task);
    }





//
//    @Operation(summary = "Deactivate task")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Task successfully deactivated",
//                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InactiveTaskDTO.class))),
//            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
//    })
//    @PostMapping("/deactivate")
//    public ResponseEntity<InactiveTaskDTO> deactivateTask(@RequestParam Integer taskId, @RequestParam(required = false) String reason) {
//        log.info("api/task/deactivate");
//        log.info("taskId: {}", taskId);
//
//        // Проверка существования задачи
//        TaskEntity task = taskService.getTaskById(taskId);
//        if (task == null) {
//            log.warn("Task with id {} not found", taskId);
//            throw new RuntimeException("Task not found");
//        }
//
//        InactiveTaskEntity inactiveTask = taskService.deactivateTask(taskId, reason);
//        InactiveTaskDTO response = new InactiveTaskDTO(inactiveTask.getId(), inactiveTask.getTask().getTaskId(), inactiveTask.getDeactivatedAt(), inactiveTask.getReason());
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
}
