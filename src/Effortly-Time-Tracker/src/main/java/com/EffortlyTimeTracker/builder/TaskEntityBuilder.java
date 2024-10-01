package com.EffortlyTimeTracker.builder;

import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.entity.TaskTagEntity;
import com.EffortlyTimeTracker.enums.Status;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class TaskEntityBuilder {
    private Integer taskId;
    private String name;
    private String description;
    private Status status;
    private Long sumTimer;
    private LocalDateTime startTimer;
    private LocalDateTime timeAddTask;
    private LocalDateTime timeEndTask;
    private TableEntity table;
    private Integer tableId;
    private Integer projectId;
    private Set<TaskTagEntity> tasks = new HashSet<>();

    public TaskEntityBuilder() {}

    public TaskEntityBuilder withTaskId(Integer taskId) {
        this.taskId = taskId;
        return this;
    }

    public TaskEntityBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TaskEntityBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskEntityBuilder withStatus(Status status) {
        this.status = status;
        return this;
    }

    public TaskEntityBuilder withSumTimer(Long sumTimer) {
        this.sumTimer = sumTimer;
        return this;
    }

    public TaskEntityBuilder withStartTimer(LocalDateTime startTimer) {
        this.startTimer = startTimer;
        return this;
    }

    public TaskEntityBuilder withTimeAddTask(LocalDateTime timeAddTask) {
        this.timeAddTask = timeAddTask;
        return this;
    }

    public TaskEntityBuilder withTimeEndTask(LocalDateTime timeEndTask) {
        this.timeEndTask = timeEndTask;
        return this;
    }

    public TaskEntityBuilder withTable(TableEntity table) {
        this.table = table;
        return this;
    }

    public TaskEntityBuilder withTableId(Integer tableId) {
        this.tableId = tableId;
        return this;
    }

    public TaskEntityBuilder withProjectId(Integer projectId) {
        this.projectId = projectId;
        return this;
    }

    public TaskEntityBuilder withTasks(Set<TaskTagEntity> tasks) {
        this.tasks = tasks;
        return this;
    }

    public TaskEntity build() {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTaskId(this.taskId);
        taskEntity.setName(this.name);
        taskEntity.setDescription(this.description);
        taskEntity.setStatus(this.status);
        taskEntity.setSumTimer(this.sumTimer);
        taskEntity.setStartTimer(this.startTimer);
        taskEntity.setTimeAddTask(this.timeAddTask);
        taskEntity.setTimeEndTask(this.timeEndTask);
        taskEntity.setTable(this.table);
        taskEntity.setProjectId(this.projectId);
        taskEntity.setTasks(this.tasks);
        return taskEntity;
    }
}
