package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.task.InactiveTaskDTO;
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
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_GUEST')")
    @CheckUserIdMatchesCurrentUserTask
    public ResponseEntity<TaskResponseDTO> addTask(@Valid @RequestBody TaskCreateDTO taskDTO) {
        log.info("api/task/add");
        log.info("taskDTO: {}", taskDTO);

        TaskEntity taskEntity = taskMapper.toEntity(taskDTO);
        log.info("taskEntity: {}", taskEntity);

        TaskEntity task = taskService.addTask(taskEntity);
        log.info("task: {}", task);


        TaskResponseDTO respons = taskMapper.toResponseDTO(task);
        log.info("respons: {}", respons);

        return new ResponseEntity<>(respons, HttpStatus.CREATED);
    }

    @Operation(summary = "Dell task by id",
            description = "need id")
    @DeleteMapping("/del")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckTaskOwner
    public void delTask(@RequestParam(required = true) Integer taskId) {
        log.info("api/task/del");
        log.info("taskId: {}", taskId);
        taskService.delTaskById(taskId);
    }

    @Operation(summary = "Get task by id",
            description = "need id")
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    @CheckTaskOwner
    public TaskResponseDTO getTask(@RequestParam(required = true) Integer taskId) {
        log.info("api/task/get");
        log.info("taskId: {}", taskId);

        TaskEntity task = taskService.getTaskById(taskId);
        log.info("task: {}", task);

        TaskResponseDTO taskResponseDTO = taskMapper.toResponseDTO(task);
        log.info("taskResponseDTO: {}", taskResponseDTO);

        return taskResponseDTO;
    }

    @Operation(summary = "Get all task")
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
    @GetMapping("/get-all-by-table-id")
    @ResponseStatus(HttpStatus.OK)
    @CheckTableOwner
    public List<TaskResponseDTO> getTaskAllByTableId(Integer id) {
        log.info("api/task/get-all-by-table-id");
        log.info("id: {}", id);

        List<TaskEntity> resEntity = taskService.getAllTaskByIdTable(id);
        log.info("resEntity: {}", resEntity);

        List<TaskResponseDTO> taskResponseDTOS = taskMapper.toResponseDTO(resEntity);
        log.info("taskResponseDTOS: {}", taskResponseDTOS);

        return taskResponseDTOS;
    }

    @Operation(summary = "Dell all task by table id", description = "need table id")
    @DeleteMapping("/del-by-table-id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckTableOwner
    public void delAllTaskByTaskId(@RequestParam(required = true) Integer id) {
        log.info("api/task/del-by-table-id");
        log.info("id: {}", id);

        taskService.delAllTaskByIdTable(id);
    }

    @Operation(summary = "Start timer", description = "need task id")
    @PostMapping("/start-timer")
    @ResponseStatus(HttpStatus.OK)
    @CheckTaskOwner
    public TaskResponseDTO startTaskTimer(@RequestParam Integer taskId) {
        log.info("api/task/start-timer");
        log.info("taskId: {}", taskId);

        TaskEntity task = taskService.startTaskTimer(taskId);
        log.info("task: {}", task);

        TaskResponseDTO taskResponseDTO = taskMapper.toResponseDTO(task);
        log.info("taskResponseDTO: {}", taskResponseDTO);
        return taskResponseDTO;
    }

    @Operation(summary = "stop timer", description = "need task id")
    @PostMapping("/stop-timer")
    @ResponseStatus(HttpStatus.OK)
    @CheckTaskOwner
    public TaskResponseDTO stopTaskTimer(@RequestParam Integer taskId) {
        log.info("api/task/stop-timer");
        log.info("taskId: {}", taskId);

        TaskEntity task = taskService.stopTaskTimer(taskId);
        log.info("task: {}", task);

        TaskResponseDTO taskResponseDTO = taskMapper.toResponseDTO(task);
        log.info("taskResponseDTO: {}", taskResponseDTO);
        return taskResponseDTO;
    }

    @Operation(summary = "Complete timer", description = "need task id")
    @PostMapping("/complete")
    @ResponseStatus(HttpStatus.OK)
    @CheckTaskOwner
    public TaskResponseDTO completeTask(@RequestParam Integer taskId) {
        log.info("api/task/complete");
        log.info("taskId: {}", taskId);

        TaskEntity task = taskService.completeTask(taskId);
        log.info("task: {}", task);

        TaskResponseDTO taskResponseDTO = taskMapper.toResponseDTO(task);
        log.info("taskResponseDTO: {}", taskResponseDTO);
        return taskResponseDTO;
    }

    @Operation(summary = "Deactivate task")
    @PostMapping("/deactivate")
    public ResponseEntity<InactiveTaskDTO> deactivateTask(@RequestParam Integer taskId, @RequestParam(required = false) String reason) {
        InactiveTaskEntity inactiveTask = taskService.deactivateTask(taskId, reason);
        // Преобразование сущности в DTO для ответа клиенту
        InactiveTaskDTO response = new InactiveTaskDTO(inactiveTask.getId(), inactiveTask.getTask().getTaskId(), inactiveTask.getDeactivatedAt(), inactiveTask.getReason());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
