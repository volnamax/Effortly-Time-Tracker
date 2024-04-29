package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.DTO.TaskDTO;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.exception.project.ProjectIsEmpty;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
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

@Slf4j
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private  final TableRepository tableRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, TableRepository tableRepository) {
        this.taskRepository = taskRepository;
        this.tableRepository = tableRepository;
    }

    public TaskEntity addTask(@NonNull TaskEntity task) {
        log.info("add new task: {}", task.getName());

        TaskEntity taskEntity = taskRepository.save(task);
        log.info("Table успешно добавлен: {}", task);
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


    public List<TaskEntity> getAllTaskByIdTable(Integer tableId) {
        TableEntity table =  tableRepository.findById(tableId).orElseThrow(()->new TableNotFoundException(tableId));
        List<TaskEntity> taskEntities= taskRepository.findByTableId(tableId);
        if (taskEntities.isEmpty())
        {
            log.info("No todos found for table  with id {}", table);
            throw new TableIsEmpty();
        }

        return taskEntities;
    }

    public void delAllTaskByIdTable(Integer tableId) {
        TableEntity table =  tableRepository.findById(tableId).orElseThrow(()->new TableNotFoundException(tableId));
        List<TaskEntity> taskEntities= taskRepository.findByTableId(tableId);

        if (taskEntities.isEmpty()) {
            log.info("No todos found for table with id {}", table);
            return;
        }
        taskRepository.deleteAll(taskEntities);
        log.info("All todos for table with id {} deleted", table);
    }

}
