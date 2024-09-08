package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.DTO.project.ProjectAnalyticsDTO;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.exception.project.ProjectIsEmpty;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.repository.IUserRepository;
import com.EffortlyTimeTracker.repository.ProjectRepository;
import com.EffortlyTimeTracker.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final IUserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, IUserRepository userRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public ProjectEntity addProject(@NotNull ProjectEntity projectEntity) {
        return projectRepository.save(projectEntity);
    }

    public void delProjectById(Integer id) {
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException(id);
        }
        projectRepository.deleteById(id);
    }

    public ProjectEntity getProjectsById(Integer id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
    }

    public List<ProjectEntity> getAllProject() {
        return projectRepository.findAll();
    }

    public List<ProjectEntity> getAllProjectByIdUser(Integer userId) {
        UserEntity user = findUserById(userId);

        List<ProjectEntity> userProjects = projectRepository.findByUserId(userId);

        if (userProjects.isEmpty()) {
            log.info("No projects found for user with id {}", userId);
            throw new ProjectIsEmpty();
        }
        return userProjects;
    }

    public void delAllProjectByIdUser(Integer userId) {
        UserEntity user = findUserById(userId);

        List<ProjectEntity> userProjects = projectRepository.findByUserId(userId);

        if (userProjects.isEmpty()) {
            log.info("No projects found for user with id {}", userId);
            return;
        }

        projectRepository.deleteAll(userProjects);
        log.info("All projects for user with id {} deleted", userId);
    }

    public ProjectAnalyticsDTO getProjectAnalytics(Integer projectId) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        List<TaskEntity> tasks = taskRepository.findByProjectId(projectId);

        List<ProjectAnalyticsDTO.TaskAnalyticsDTO> taskAnalytics = tasks.stream()
                .map(task -> {
                    ProjectAnalyticsDTO.TaskAnalyticsDTO dto = new ProjectAnalyticsDTO.TaskAnalyticsDTO();
                    dto.setTaskId(task.getTaskId());
                    dto.setTaskName(task.getName());
                    dto.setStartDate(task.getTimeAddTask());
                    dto.setEndDate(task.getTimeEndTask());
                    dto.setTimeSpent(task.getSumTimer());
                    return dto;
                })
                .collect(Collectors.toList());

        List<Long> timeSpentList = taskAnalytics.stream()
                .map(ProjectAnalyticsDTO.TaskAnalyticsDTO::getTimeSpent)
                .sorted()
                .collect(Collectors.toList());

        long totalTimeSpent = timeSpentList.stream().mapToLong(Long::longValue).sum();
        long averageTimeSpent = totalTimeSpent / timeSpentList.size();
        long maxTimeSpent = timeSpentList.stream().max(Comparator.naturalOrder()).orElse(0L);
        long minTimeSpent = timeSpentList.stream().min(Comparator.naturalOrder()).orElse(0L);
        long medianTimeSpent = timeSpentList.size() % 2 == 0
                ? (timeSpentList.get(timeSpentList.size() / 2 - 1) + timeSpentList.get(timeSpentList.size() / 2)) / 2
                : timeSpentList.get(timeSpentList.size() / 2);

        Optional<ProjectAnalyticsDTO.TaskAnalyticsDTO> longestTask = taskAnalytics.stream()
                .max(Comparator.comparingLong(ProjectAnalyticsDTO.TaskAnalyticsDTO::getTimeSpent));

        ProjectAnalyticsDTO analyticsDTO = new ProjectAnalyticsDTO();
        analyticsDTO.setProjectId(project.getProjectId());
        analyticsDTO.setProjectName(project.getName());
        analyticsDTO.setTasks(taskAnalytics);
        analyticsDTO.setAverageTimeSpent(averageTimeSpent);
        analyticsDTO.setMaxTimeSpent(maxTimeSpent);
        analyticsDTO.setMinTimeSpent(minTimeSpent);
        analyticsDTO.setMedianTimeSpent(medianTimeSpent);
        analyticsDTO.setLongestTask(longestTask.orElse(null));

        return analyticsDTO;
    }

    private UserEntity findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoudException(userId));
    }
}
