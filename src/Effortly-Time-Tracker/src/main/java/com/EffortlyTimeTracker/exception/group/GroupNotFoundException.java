package com.EffortlyTimeTracker.exception.group;

import com.EffortlyTimeTracker.exception.BaseException;

public class GroupNotFoundException extends BaseException {
    public GroupNotFoundException(Integer id) {
        super("Group with id " + id + " not found");
    }
}
