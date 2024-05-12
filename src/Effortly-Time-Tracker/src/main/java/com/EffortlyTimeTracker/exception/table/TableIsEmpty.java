package com.EffortlyTimeTracker.exception.table;

import com.EffortlyTimeTracker.exception.BaseException;
import com.EffortlyTimeTracker.repository.TableRepository;

public class TableIsEmpty extends BaseException {
    public TableIsEmpty() {
        super(("Table is empty"));
    }

}
