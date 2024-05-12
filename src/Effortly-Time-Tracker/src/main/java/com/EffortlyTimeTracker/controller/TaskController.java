package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.task.TaskCreateDTO;
import com.EffortlyTimeTracker.DTO.task.TaskResponseDTO;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.mapper.TaskMapper;
import com.EffortlyTimeTracker.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@Tag(name = "Task-controller")
@RestController
@RequestMapping("api/task")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @Operation(summary = "Add task", description = "need name and id table, status = ACTIVE NO_ACTIVEl")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
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
    public void delTask(@RequestParam(required = true) Integer taskId) {
        log.info("api/task/del");
        log.info("taskId: {}", taskId);
        taskService.delTaskById(taskId);
    }

    @Operation(summary = "Get task by id",
            description = "need id")
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDTO getTask(@RequestParam(required = true) Integer taskId) {
        log.info("api/task/get");
        log.info("taskId: {}", taskId);

        TaskEntity task =taskService.getTaskById(taskId);
        log.info("task: {}", task);

        TaskResponseDTO taskResponseDTO =  taskMapper.toResponseDTO(task);
        log.info("taskResponseDTO: {}", taskResponseDTO);

        return taskResponseDTO;
    }

    @Operation(summary = "Get all task")
    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponseDTO> getTask() {
        log.info("api/task/get-all");
        List<TaskEntity> tasks=  taskService.getAllTask();
        log.info("tasks: {}", tasks);

        List<TaskResponseDTO> taskResponseDTOS = taskMapper.toResponseDTO(tasks);
        log.info("taskResponseDTOS: {}", taskResponseDTOS);

        return taskResponseDTOS;
    }
    @Operation(summary = "Get all task by id table", description = "need table id")
    @GetMapping("/get-all-by-table-id")
    @ResponseStatus(HttpStatus.OK)
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
    public void delAllTaskByTaskId(@RequestParam(required = true) Integer id) {
        log.info("api/task/del-by-table-id");
        log.info("id: {}", id);

        taskService.delAllTaskByIdTable(id);
    }

}
