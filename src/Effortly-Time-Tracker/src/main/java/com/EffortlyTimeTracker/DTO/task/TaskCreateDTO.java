package com.EffortlyTimeTracker.DTO.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

//todo check add whihout table (no table)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskCreateDTO {
    @NotBlank(message = "name task is empty")
    String name;

    String description;

    String status;

    Long sumTimer;

    LocalDateTime startTimer;

    LocalDateTime timeAddTask;

    LocalDateTime timeEndTask;

    @NotNull(message = "table in task is empty")
    Integer tableId;

//    Set<Integer> tagsId;
}
