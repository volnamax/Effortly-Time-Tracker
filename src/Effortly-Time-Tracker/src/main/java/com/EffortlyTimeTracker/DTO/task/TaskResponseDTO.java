package com.EffortlyTimeTracker.DTO.task;

import com.EffortlyTimeTracker.entity.TableEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskResponseDTO {
    Integer taskId;
    @NotBlank(message = "name task is empty")
    String name;

    String description;

    String status;

    Long sumTimer;

    LocalDateTime startTimer;


    LocalDateTime timeAddTask;
    private Integer projectId;    // Добавляем projectId
    private String projectName;   // Добавляем projectName
    LocalDateTime timeEndTask;

    @NotNull(message = "table in task is empty")
    Integer tableId;
    Set<Integer> tags;
}
