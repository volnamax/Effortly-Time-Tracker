package com.EffortlyTimeTracker.DTO;

import com.EffortlyTimeTracker.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupDTO {
    @NotBlank
    String name;
    String description;

    @NonNull
    Set<UserEntity> usersGroup;

    Integer projectId;

    @Override
    public String toString() {
        return "GroupDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", projectId=" + projectId +
                '}';
    }
}


