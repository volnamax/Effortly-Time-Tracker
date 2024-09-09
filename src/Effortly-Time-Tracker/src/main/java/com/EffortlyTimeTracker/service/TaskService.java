package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.InactiveTaskEntity;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.enums.Status;
import com.EffortlyTimeTracker.exception.table.TableIsEmpty;
import com.EffortlyTimeTracker.exception.table.TableNotFoundException;
import com.EffortlyTimeTracker.exception.task.TaskNotFoundException;
import com.EffortlyTimeTracker.repository.postgres.InactiveTaskRepository;
import com.EffortlyTimeTracker.repository.postgres.TableRepository;
import com.EffortlyTimeTracker.repository.postgres.TaskRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

//todo think about empty table (is exception ?)

@Slf4j
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TableRepository tableRepository;
    private final InactiveTaskRepository inactiveTaskRepository;


    @Autowired
    public TaskService(TaskRepository taskRepository, TableRepository tableRepository,InactiveTaskRepository  inactiveTaskRepository) {
        this.taskRepository = taskRepository;
        this.tableRepository = tableRepository;
        this.inactiveTaskRepository = inactiveTaskRepository;
    }

    public TaskEntity addTask(@NonNull TaskEntity task) {
        task.setTimeAddTask(LocalDateTime.now());
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

    public TaskEntity startTaskTimer(Integer taskId) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (task.getStartTimer() != null) {
            throw new IllegalStateException("Timer is already running for this task.");
        }

        task.setStartTimer(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public TaskEntity stopTaskTimer(Integer taskId) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (task.getStartTimer() == null) {
            throw new IllegalStateException("Timer is not running for this task.");
        }

        LocalDateTime now = LocalDateTime.now();
        long duration = java.time.Duration.between(task.getStartTimer(), now).getSeconds();

        task.setSumTimer(task.getSumTimer() == null ? duration : task.getSumTimer() + duration);
        task.setStartTimer(null);

        return taskRepository.save(task);
    }

    public TaskEntity completeTask(Integer taskId) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (task.getStatus() == Status.NO_ACTIVE) {
            throw new IllegalStateException("Task is already completed.");
        }

        // Если таймер запущен, останавливаем его
        if (task.getStartTimer() != null) {
            LocalDateTime now = LocalDateTime.now();
            long duration = java.time.Duration.between(task.getStartTimer(), now).getSeconds();
            task.setSumTimer(task.getSumTimer() == null ? duration : task.getSumTimer() + duration);
            task.setStartTimer(null);
        }

        task.setStatus(Status.NO_ACTIVE);
        task.setTimeEndTask(LocalDateTime.now());
        return taskRepository.save(task);
    }


    public InactiveTaskEntity deactivateTask(Integer taskId, String reason) {
        // Найти задачу по ID
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        // Проверка, что задача еще активна
        if (task.getStatus() == Status.NO_ACTIVE) {
            throw new IllegalStateException("Task is already deactivated.");
        }

        // Создание записи в inactive_task
        InactiveTaskEntity inactiveTask = InactiveTaskEntity.builder()
                .task(task)
                .deactivatedAt(LocalDateTime.now())
                .reason(reason)
                .build();

        // Изменение статуса задачи на NO_ACTIVE
        task.setStatus(Status.NO_ACTIVE);
        task.setTimeEndTask(LocalDateTime.now());
        taskRepository.save(task);  // Обновление записи в таблице task

        // Сохранение записи в inactive_task
        return inactiveTaskRepository.save(inactiveTask);
    }
}
