package com.EffortlyTimeTracker.exception.todo;

import com.EffortlyTimeTracker.exception.BaseException;

public class InvalidTodoException extends BaseException {
    public InvalidTodoException(String msg) {
        super(msg);
    }
}
