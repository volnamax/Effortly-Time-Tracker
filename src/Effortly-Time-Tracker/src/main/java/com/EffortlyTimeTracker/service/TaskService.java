package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.DTO.TaskDTO;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.exception.task.TaskNotFoundException;
import com.EffortlyTimeTracker.repository.TaskRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskEntity addTask(@NonNull TaskDTO taskDTO) {
        log.info("add new task: {}", taskDTO.getName());
        TaskEntity task = taskRepository.save(TaskEntity.builder()
                .name(taskDTO.getName())
                .description(taskDTO.getDescription())
//                .status(taskDTO.getStatus())
                .sumTimer(taskDTO.getSumTimer())
                .startTimer(taskDTO.getStartTimer())
                .timeAddTask(taskDTO.getTimeAddTask())
                .timeEndTask(taskDTO.getTimeEndTask())
//                .tags(taskDTO.getTags())
                .table(taskDTO.getTable())
                .build());
        log.info("task added : {}", task.getTaskId());
        return task;
    }

    public void delTaskById(Integer taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException(taskId);
        }
        taskRepository.deleteById(taskId);
        log.info("Task with id {} deleted", taskId);
    }

    public TaskEntity getTaskById(Integer taskId) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        log.info("Get = " + task);
        return task;
    }

    public List<TaskEntity> getAllTask() {
        List<TaskEntity> tasks = taskRepository.findAll();
        log.info("GetALL = " + tasks);
        return tasks;
    }
}
