package com.EffortlyTimeTracker.DTO;

import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagDTO {
    @NotBlank String name;
    String color;
    Set<TaskEntity> tasks;

    @NotNull(message = "Project must be provided")
    ProjectEntity project;


}
