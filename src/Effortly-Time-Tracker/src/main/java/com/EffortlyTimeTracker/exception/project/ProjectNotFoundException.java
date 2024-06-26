package com.EffortlyTimeTracker.exception.project;

import com.EffortlyTimeTracker.exception.BaseException;

public class ProjectNotFoundException extends BaseException {
    public ProjectNotFoundException(Integer id) {
        super("Project with ID " + id + " not found");
    }
}
