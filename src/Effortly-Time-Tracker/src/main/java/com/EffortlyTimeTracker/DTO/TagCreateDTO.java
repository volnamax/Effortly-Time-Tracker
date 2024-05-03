package com.EffortlyTimeTracker.DTO;

import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagCreateDTO {
    @NotBlank
    String name;
    String color;
    Integer taskId;

    @NotNull(message = "Project id is null")
    Integer projectId;
}
