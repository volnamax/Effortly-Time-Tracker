package com.EffortlyTimeTracker.DTO.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagCreateDTO {
    @NotBlank(message = "no name in tag")
    String name;
    String color;
    Integer taskId;

    @NotNull(message = "Project id is null")
    Integer projectId;
}
