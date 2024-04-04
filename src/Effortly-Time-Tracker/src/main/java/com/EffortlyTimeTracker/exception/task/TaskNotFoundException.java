package com.EffortlyTimeTracker.exception.task;

import com.EffortlyTimeTracker.exception.BaseException;

public class TaskNotFoundException extends BaseException {
    public TaskNotFoundException(Integer id) {
        super("Task with ID " + id + " not found");
    }
}
