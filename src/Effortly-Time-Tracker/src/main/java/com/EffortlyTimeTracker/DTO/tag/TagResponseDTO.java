
package com.EffortlyTimeTracker.DTO.tag;

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
public class TagResponseDTO {
    Integer tagId;
    @NotBlank(message = "no name in tag")
    String name;
    String color;
    Set<Integer> tasks;

    @NotNull(message = "Project must be provided")
    Integer projectID;
}
