package com.EffortlyTimeTracker.exception.group;

import com.EffortlyTimeTracker.exception.BaseException;

public class GroupNotFoundException extends BaseException {
    public GroupNotFoundException(Integer id) {
        super("Project with ID " + id + " not found");
    }
}
