package com.EffortlyTimeTracker.exception.user;

import com.EffortlyTimeTracker.exception.BaseException;

public class UserNotRoleException extends BaseException {
    public UserNotRoleException() {
        super("User with id not found role");
    }
}
