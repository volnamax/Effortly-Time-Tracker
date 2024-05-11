package com.EffortlyTimeTracker.DTO.table;
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
public class TableResponseDTO {
    @NotBlank(message = "Error table name is empty")
    String name;

    String description;

    String status;

    @NotNull(message = "Error table not have project")
    Integer projectId;

    List<Integer> tasks;
}
