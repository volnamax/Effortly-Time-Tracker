package com.EffortlyTimeTracker.builder;

import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Priority;
import com.EffortlyTimeTracker.enums.Status;

import java.time.LocalDateTime;

public class TodoNodeEntityBuilder {
    private Integer todoId = 1;
    private String content = "Default content";
    private Status status = Status.ACTIVE;
    private Priority priority = Priority.IMPORTANT_URGENTLY;
    private LocalDateTime dueData = LocalDateTime.now().plusDays(7);
    private UserEntity user = new UserEntity();
    private Integer userId = 1;

    public TodoNodeEntityBuilder withId(Integer todoId) {
        this.todoId = todoId;
        return this;
    }

    public TodoNodeEntityBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public TodoNodeEntityBuilder withStatus(Status status) {
        this.status = status;
        return this;
    }

    public TodoNodeEntityBuilder withPriority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public TodoNodeEntityBuilder withDueData(LocalDateTime dueData) {
        this.dueData = dueData;
        return this;
    }

    public TodoNodeEntityBuilder withUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public TodoNodeEntityBuilder withUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public TodoNodeEntity build() {
        TodoNodeEntity todoNodeEntity = new TodoNodeEntity();
        todoNodeEntity.setTodoId(this.todoId);
        todoNodeEntity.setContent(this.content);
        todoNodeEntity.setStatus(this.status);
        todoNodeEntity.setPriority(this.priority);
        todoNodeEntity.setDueData(this.dueData);
        todoNodeEntity.setUser(this.user);
        todoNodeEntity.setUserId(this.userId);
        return todoNodeEntity;
    }
}
