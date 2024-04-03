package com.EffortlyTimeTracker.DTO;


import com.EffortlyTimeTracker.entity.User;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TodoNodeDTO {
    @NotBlank(message = "Error todo node content  is empty")
    String content;

    @NotBlank(message = "Error todo node status  is empty")
    String status;
    @NotBlank(message = "Error todo node priority  is empty")
    String priority;

    LocalDateTime dueData;

    User userTodo;
}
