package com.EffortlyTimeTracker.DTO.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupCreateDTO {
    @NotBlank(message = "Group name is empty")
    String name;
    String description;
    @NotNull(message = "Group project is empty")
    Integer projectId;
}


