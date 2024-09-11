package com.EffortlyTimeTracker.exception.table;

import com.EffortlyTimeTracker.exception.BaseException;

public class TableIsEmpty extends BaseException {
    public TableIsEmpty() {
        super(("Table is empty"));
    }

}
