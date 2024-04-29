package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.TableDTO;
import com.EffortlyTimeTracker.DTO.TaskDTO;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.mapper.TaskMapper;
import com.EffortlyTimeTracker.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "Add task")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskDTO> addTask(@Valid @RequestBody TaskDTO taskDTO) {
        TaskEntity taskEntity = taskMapper.dtoToEntity(taskDTO);
        TaskEntity task = taskService.addTask(taskEntity);
        TaskDTO respons = taskMapper.entityToDto(task);
        return new ResponseEntity<>(respons, HttpStatus.CREATED);
    }

    @Operation(summary = "Dell task by id",
            description = "need id")
    @DeleteMapping("/del")
    public void delTask(@RequestParam(required = true) Integer taskId) {
        taskService.delTaskById(taskId);
    }

    @Operation(summary = "Get task by id",
            description = "need id")
    @GetMapping("/get")
    public TaskDTO getTask(@RequestParam(required = true) Integer taskId) {
        TaskEntity task =taskService.getTaskById(taskId);
        return taskMapper.entityToDto(task);
    }

    @Operation(summary = "Get all task")
    @GetMapping("/get-all")
    public List<TaskDTO> getTask() {
        List<TaskEntity> tasks=  taskService.getAllTask();
        return taskMapper.entityListToDtoList(tasks);
    }
    @Operation(summary = "Get all task by id table", description = "need table id")
    @GetMapping("/get-all-by-table-id")
    public List<TaskDTO> getTaskAllByTableId(Integer id) {
        List<TaskEntity> resEntity = taskService.getAllTaskByIdTable(id);
        return taskMapper.entityListToDtoList(resEntity);
    }

    @Operation(summary = "Dell all task by table id", description = "need table id")
    @DeleteMapping("/del-by-table-id")
    public void delAllTaskByTaskId(@RequestParam(required = true) Integer id) {
        taskService.delAllTaskByIdTable(id);
    }

}
