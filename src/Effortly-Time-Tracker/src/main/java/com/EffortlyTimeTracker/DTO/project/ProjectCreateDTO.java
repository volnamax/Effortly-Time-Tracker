package com.EffortlyTimeTracker.DTO.project;

import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectCreateDTO {
    @NotBlank(message = "Error proj name is empty")
    String name;

    String description;
    @NotNull(message = "Error proj not have user id")
    Integer userProject;

    Integer groupId;

    //todo del in future
    Integer tableId;
    Integer tagId;

}
