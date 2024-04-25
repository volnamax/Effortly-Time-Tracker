package com.EffortlyTimeTracker.exception.todo;

import com.EffortlyTimeTracker.exception.BaseException;

public class TodoNotFoudException extends BaseException {
    public TodoNotFoudException(Integer id) {
        super("Todo with id " + id + " not found");
    }

}
