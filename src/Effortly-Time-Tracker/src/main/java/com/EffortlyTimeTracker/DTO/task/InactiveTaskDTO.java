package com.EffortlyTimeTracker.DTO.task;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class InactiveTaskDTO {
    private Integer id;
    private Integer taskId;
    private LocalDateTime deactivatedAt;
    private String reason;
}