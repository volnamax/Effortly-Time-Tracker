package com.EffortlyTimeTracker.exception.user;

import com.EffortlyTimeTracker.exception.BaseException;

public class UserNotFoudException extends BaseException {
    public UserNotFoudException(Integer id) {
        super("user with id " + id + "not found");
    }
}
