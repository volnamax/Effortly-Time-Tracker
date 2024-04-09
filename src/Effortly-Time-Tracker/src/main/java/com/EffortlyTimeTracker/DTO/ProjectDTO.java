package com.EffortlyTimeTracker.DTO;

import com.EffortlyTimeTracker.entity.GroupEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectDTO {
    @NotBlank(message = "Error proj name is empty")
    String name;
    String description;
    @NotNull(message = "Error proj not have user")
    UserEntity userProject;
//    List<TableProject> tables;
//    List<TagProject> tags;
    GroupEntity groupProject;
}
