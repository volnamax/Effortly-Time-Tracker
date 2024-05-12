package com.EffortlyTimeTracker.exception.table;

import com.EffortlyTimeTracker.exception.BaseException;

public class TableNotFoundException extends BaseException {
    public TableNotFoundException(Integer id) {
        super("Table with ID " + id + " not found");
    }
}
