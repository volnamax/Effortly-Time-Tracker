package com.EffortlyTimeTracker.exception.tag;

import com.EffortlyTimeTracker.exception.BaseException;

public class TagNotFoundException extends BaseException {
    public TagNotFoundException(Integer id) {
        super("Tag with ID " + id + " not found");
    }
}
