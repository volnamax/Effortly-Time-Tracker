package com.EffortlyTimeTracker.DTO;

import com.EffortlyTimeTracker.entity.Group;
import com.EffortlyTimeTracker.entity.TableProject;
import com.EffortlyTimeTracker.entity.TagProject;
import com.EffortlyTimeTracker.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectDTO {
    @NotBlank(message = "Error proj name is empty")
    String name;
    String description;
    @NotNull(message = "Error proj not have user")
    User userProject;
    List<TableProject> tables;
    List<TagProject> tags;
    Group groupP;
}
