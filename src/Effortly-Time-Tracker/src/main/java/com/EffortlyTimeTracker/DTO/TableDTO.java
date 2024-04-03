package com.EffortlyTimeTracker.DTO;

import com.EffortlyTimeTracker.entity.Project;
import com.EffortlyTimeTracker.entity.TaskTable;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableDTO {
    @NotBlank(message = "Error table name is empty")
    String name;

    String description;

    String status;

//    @NotBlank(message = "Error table not have project")
    Project project;

    List<TaskTable> tasks;
}
