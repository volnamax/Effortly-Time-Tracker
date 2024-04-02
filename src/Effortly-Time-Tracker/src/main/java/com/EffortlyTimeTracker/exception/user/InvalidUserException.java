package com.EffortlyTimeTracker.exception.user;

import com.EffortlyTimeTracker.exception.BaseException;

public class InvalidUserException extends BaseException {
    public InvalidUserException(String msg) {
        super(msg);
    }
}