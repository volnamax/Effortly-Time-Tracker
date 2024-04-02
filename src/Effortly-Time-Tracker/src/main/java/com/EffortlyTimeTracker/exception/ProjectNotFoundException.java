package com.EffortlyTimeTracker.exception;

public class ProjectNotFoundException extends BaseException {
    public ProjectNotFoundException(Integer id) {
        super("Проект с ID " + id + " не найден");
    }
}
