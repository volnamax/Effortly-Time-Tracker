package com.EffortlyTimeTracker.DTO.project;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectAnalyticsDTO {
    private Integer projectId;
    private String projectName;
    private List<TaskAnalyticsDTO> tasks;
    private Long averageTimeSpent;
    private Long maxTimeSpent;
    private Long minTimeSpent;
    private Long medianTimeSpent;
    private TaskAnalyticsDTO longestTask;

    @Data
    public static class TaskAnalyticsDTO {
        private Integer taskId;
        private String taskName;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Long timeSpent; // Time spent in milliseconds
    }
}
