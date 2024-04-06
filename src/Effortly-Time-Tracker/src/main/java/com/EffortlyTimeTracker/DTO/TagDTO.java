package com.EffortlyTimeTracker.DTO;

import com.EffortlyTimeTracker.entity.Project;
import com.EffortlyTimeTracker.entity.TaskTable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagDTO {
    @NonNull String name;
    String color;
    @NonNull
    Project project;
    Set<TaskTable> tasks;
}
