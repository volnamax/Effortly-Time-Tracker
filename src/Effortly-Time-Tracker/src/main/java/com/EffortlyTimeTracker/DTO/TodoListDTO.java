package com.EffortlyTimeTracker.DTO;


import com.EffortlyTimeTracker.entity.User;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TodoListDTO {
    @NotBlank(message = "Error todo list content  is empty")
    String content;

    @NotBlank(message = "Error todo list status  is empty")
    String status;

    String priority;

    LocalDateTime dueData;

    User userTodo;
}
