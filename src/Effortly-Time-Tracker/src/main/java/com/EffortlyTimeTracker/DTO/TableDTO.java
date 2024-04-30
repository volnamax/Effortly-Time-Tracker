package com.EffortlyTimeTracker.DTO;
//todo del entity in dto
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Error table not have project")
    ProjectEntity project;

    List<TaskEntity> tasks;
}
