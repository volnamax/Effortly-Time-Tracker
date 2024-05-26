package com.EffortlyTimeTracker.service.middlewareOwn.task;

import com.EffortlyTimeTracker.DTO.task.TaskCreateDTO;
import com.EffortlyTimeTracker.controller.TaskController;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.service.ProjectService;
import com.EffortlyTimeTracker.service.TableService;
import com.EffortlyTimeTracker.service.TaskService;
import com.EffortlyTimeTracker.service.UserService;
import com.EffortlyTimeTracker.service.middlewareOwn.OwnerOperation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
@Slf4j
public class OwnershipTask {
    private final UserService userService;
    private final TaskService taskService;
    private final ProjectService projectService;
    private final TableService tableService;
    private final OwnerOperation ownerOperation;

    @Autowired
    public OwnershipTask(@NonNull UserService userService, TaskService taskService, OwnerOperation ownerOperation, ProjectService projectService, TableService tableService) {
        this.userService = userService;
        this.tableService = tableService;
        this.taskService = taskService;
        this.projectService = projectService;
        this.ownerOperation = ownerOperation;
    }

    @Before("@annotation(CheckTaskOwner) && args(taskId,..)")
    public void checkTaskOwnership(JoinPoint joinPoint, Integer taskId) {
        log.info("+ Checking task ownership for task {}", taskId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ownerOperation.isAdmin(authentication) || ownerOperation.isTaskOwner(authentication, taskId, userService, taskService)) {
            log.info("+ Authorization successful for task {}", taskId);
        } else {
            log.warn("+ Authorization failed for task {}", taskId);
            throw new AccessDeniedException("You are not authorized to access this task");
        }
    }

    @Before("@annotation(CheckUserIdMatchesCurrentUserTask) && args(taskDTO,..)")
    public void checkUserIdMatchesCurrentUserOwnership(JoinPoint joinPoint, TaskCreateDTO taskDTO) {
        TableEntity table = tableService.getTableById(taskDTO.getTableId());
        ProjectEntity project = projectService.getProjectsById(table.getProject().getProjectId());
        Integer idOwner = project.getUserProject().getUserId();
        log.info("+ Checking userId matches current user for task {}", idOwner);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            log.warn("+ User is not authenticated");
            throw new AccessDeniedException("You are not authorized to perform this action");
        }
        if (!ownerOperation.isCurrentUser(authentication, idOwner, userService) && !ownerOperation.isAdmin(authentication)) {
            log.warn("+ User is not authorized to perform this action for userId {}",idOwner);
            throw new AccessDeniedException("You are not authorized to perform this action for this user");
        }
        log.info("+ Authorization successful for userId {}", idOwner);
    }


}
