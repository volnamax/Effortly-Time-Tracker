package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.exception.table.TableIsEmpty;
import com.EffortlyTimeTracker.exception.table.TableNotFoundException;
import com.EffortlyTimeTracker.exception.task.TaskNotFoundException;
import com.EffortlyTimeTracker.repository.TableRepository;
import com.EffortlyTimeTracker.repository.TaskRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//todo think about empty table (is exception ?)

@Slf4j
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TableRepository tableRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, TableRepository tableRepository) {
        this.taskRepository = taskRepository;
        this.tableRepository = tableRepository;
    }

    public TaskEntity addTask(@NonNull TaskEntity task) {
        return taskRepository.save(task);
    }

    public void delTaskById(Integer taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException(taskId);
        }
        taskRepository.deleteById(taskId);
        log.info("Task with id {} deleted", taskId);
    }

    public TaskEntity getTaskById(Integer taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    public List<TaskEntity> getAllTask() {
        return  taskRepository.findAll();
    }


    public List<TaskEntity> getAllTaskByIdTable(Integer tableId) {
        TableEntity table = tableRepository.findById(tableId).orElseThrow(() -> new TableNotFoundException(tableId));
        List<TaskEntity> taskEntities = taskRepository.findByTableId(tableId);
        if (taskEntities.isEmpty()) {
            log.info("No todos found for table  with id {}", table);
            throw new TableIsEmpty();
        }

        return taskEntities;
    }

    public void delAllTaskByIdTable(Integer tableId) {
        TableEntity table = tableRepository.findById(tableId).orElseThrow(() -> new TableNotFoundException(tableId));
        List<TaskEntity> taskEntities = taskRepository.findByTableId(tableId);

        if (taskEntities.isEmpty()) {
            log.info("No todos found for table with id {}", table);
            return;
        }
        taskRepository.deleteAll(taskEntities);
        log.info("All todos for table with id {} deleted", table);
    }

}
