package com.EffortlyTimeTracker.DTO;

import com.EffortlyTimeTracker.entity.TableProject;
import com.EffortlyTimeTracker.entity.TagProject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskDTO {
    @NotBlank(message = "name task is empty")
    String name;

    String description;

    String status;

    Long sumTimer;

    LocalDateTime startTimer;

    LocalDateTime timeAddTask;

    LocalDateTime timeEndTask;

    @NotNull
    TableProject table;

    Set<TagProject> tags;


}
