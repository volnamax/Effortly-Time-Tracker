package com.EffortlyTimeTracker.DTO.todDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    //UserEntity
    @NotNull(message = "Error todo node not have user id")
    Integer userID;

    LocalDateTime dueData;
}
