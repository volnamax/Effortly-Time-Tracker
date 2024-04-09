package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.TaskDTO;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Task-controller")
@RestController
@RequestMapping("api/task")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Add task")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskEntity addTask(@Valid @RequestBody TaskDTO taskDTO) {
        return taskService.addTask(taskDTO);
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
    public TaskEntity getTask(@RequestParam(required = true) Integer  taskId) {
        return taskService.getTaskById(taskId);
    }
    @Operation(summary = "Get all task")
    @GetMapping("/get-all")
    public List<TaskEntity> getTask() {
        return taskService.getAllTask();
    }

}
