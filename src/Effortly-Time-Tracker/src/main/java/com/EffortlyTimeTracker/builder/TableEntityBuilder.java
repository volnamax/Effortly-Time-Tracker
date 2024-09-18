package com.EffortlyTimeTracker.builder;

import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.enums.Status;

import java.util.List;

public class TableEntityBuilder {
    private Integer tableId;
    private String name;
    private String description;
    private Status status;
    private ProjectEntity project;
    private Integer projectId;
    private List<TaskEntity> tasks;

    public TableEntityBuilder() {
    }

    public TableEntityBuilder withTableId(Integer tableId) {
        this.tableId = tableId;
        return this;
    }

    public TableEntityBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TableEntityBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TableEntityBuilder withStatus(Status status) {
        this.status = status;
        return this;
    }

    public TableEntityBuilder withProject(ProjectEntity project) {
        this.project = project;
        this.projectId = project != null ? project.getProjectId() : null;
        return this;
    }

    public TableEntityBuilder withProjectId(Integer projectId) {
        this.projectId = projectId;
        return this;
    }

    public TableEntityBuilder withTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
        return this;
    }

    public TableEntity build() {
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableId(this.tableId);
        tableEntity.setName(this.name);
        tableEntity.setDescription(this.description);
        tableEntity.setStatus(this.status);
        tableEntity.setProject(this.project);
        tableEntity.setProjectId(this.projectId);
        tableEntity.setTasks(this.tasks);
        return tableEntity;
    }
}
