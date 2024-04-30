package com.EffortlyTimeTracker.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupDTO {
    @NotBlank(message = "Group name is empty")
    String name;
    String description;

    Set<Integer> users;
    @NotNull(message = "Group project is empty")
    Integer projectId;
}


