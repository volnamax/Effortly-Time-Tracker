package com.EffortlyTimeTracker.service.middlewareOwn;

import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.service.ProjectService;
import com.EffortlyTimeTracker.service.TaskService;
import com.EffortlyTimeTracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class OwnerOperation {

    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }


    public boolean isCurrentUser(Authentication authentication, Integer userId, UserService userService) {
        log.info("+ Checking owner of user " + userId);

        String currentUserEmail = authentication.getName(); // email as username
        log.info("+ Current user email: {}", currentUserEmail);
        Optional<UserEntity> userEntity = userService.getUserByEmail(currentUserEmail);
        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            Integer id = user.getUserId();

            log.info("+ Current user id: {}", id);
            return id.equals(userId);
        }
        return false;
    }

    public boolean isProjectOwner(Authentication authentication, Integer projectId, UserService userService, ProjectService projectService) {
        String currentUserEmail = authentication.getName();
        Optional<UserEntity> userEntity = userService.getUserByEmail(currentUserEmail);
        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            Integer userId = user.getUserId();

            ProjectEntity projectEntity = projectService.getProjectsById(projectId);
            Integer projectOwnerId = projectEntity.getUserProject().getUserId();

            return projectOwnerId.equals(userId);
        }
        return false;
    }

    public boolean isTaskOwner(Authentication authentication, Integer taskId, UserService userService, TaskService taskService) {
        log.info("+ isTaskOwner Checking if user is owner of task {}", taskId);
        String currentUserEmail = authentication.getName();
        log.info("+ Current user email: {}", currentUserEmail);
        Optional<UserEntity> userEntity = userService.getUserByEmail(currentUserEmail);
        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            Integer userId = user.getUserId();
            log.info("+ Current user ID: {}", userId);

            TaskEntity taskEntity = taskService.getTaskById(taskId);
            log.info("+ Current project ID: {}", taskEntity.getProjectId());


            Integer taskOwnerId = taskEntity.getTable().getProject().getUserProject().getUserId();
            log.info("+ Task owner ID: {}", taskOwnerId);

            return taskOwnerId.equals(userId);
        }
        log.warn("+ User entity not found for email {}", currentUserEmail);
        return false;
    }
}
