package com.EffortlyTimeTracker.DTO;

import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupDTO {
    @NonNull
    String name;
    String description;
    @NonNull
    Set<UserEntity> usersGroup;
    @NonNull
    ProjectEntity project;
}


