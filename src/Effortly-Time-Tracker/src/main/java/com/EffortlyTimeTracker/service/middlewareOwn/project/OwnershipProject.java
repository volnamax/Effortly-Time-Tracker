package com.EffortlyTimeTracker.service.middlewareOwn.project;

import com.EffortlyTimeTracker.DTO.project.ProjectCreateDTO;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.service.ProjectService;
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
public class OwnershipProject {
    private final UserService userService;
    private final ProjectService projectService;
    private final OwnerOperation ownerOperation;

    @Autowired
    public OwnershipProject(@NonNull UserService userService, ProjectService projectService, OwnerOperation ownerOperation) {
        this.userService = userService;
        this.projectService = projectService;
        this.ownerOperation = ownerOperation;
    }

    @Before("@annotation(CheckProjectOwner) && args(projectId,..)")
    public void checkProjectOwnership(JoinPoint joinPoint, Integer projectId) {
        log.info("+ Checking project ownership for project {}", projectId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ownerOperation.isAdmin(authentication) || isProjectOwner(authentication, projectId)) {
            log.info("+ Authorization successful for project {}", projectId);
        } else {
            log.warn("+ Authorization failed for project {}", projectId);
            throw new AccessDeniedException("You are not authorized to access this project");
        }
    }

    @Before("@annotation(CheckUserIdMatchesCurrentUserProject) && args(projectDTO,..)")
    public void checkUserIdMatchesCurrentUserOwnership(JoinPoint joinPoint, ProjectCreateDTO projectDTO) {
        log.info("+ Checking userId matches current user for project {}", projectDTO.getUserProject());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            log.warn("+ User is not authenticated");
            throw new AccessDeniedException("You are not authorized to perform this action");
        }
        if (!ownerOperation.isCurrentUser(authentication, projectDTO.getUserProject(), userService) && !ownerOperation.isAdmin(authentication)) {
            log.warn("+ User is not authorized to perform this action for userId {}", projectDTO.getUserProject());
            throw new AccessDeniedException("You are not authorized to perform this action for this user");
        }
        log.info("+ Authorization successful for userId {}", projectDTO.getUserProject());
    }

    @Before("@annotation(CheckUserIdMatchesCurrentUserProject) && args(userId,..)")
    public void checkUserIdMatchesCurrentUserOwnership(JoinPoint joinPoint, Integer userId) {
        log.info("+ Checking userId matches current user for project {}", userId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            log.warn("+ User is not authenticated");
            throw new AccessDeniedException("You are not authorized to perform this action");
        }
        if (!ownerOperation.isCurrentUser(authentication, userId, userService) && !ownerOperation.isAdmin(authentication)) {
            log.warn("+ User is not authorized to perform this action for userId {}", userId);
            throw new AccessDeniedException("You are not authorized to perform this action for this user");
        }
        log.info("+ Authorization successful for userId {}", userId);
    }

    private boolean isProjectOwner(Authentication authentication, Integer projectId) {
        log.info("+ Checking if user is owner of project {}", projectId);
        String currentUserEmail = authentication.getName();
        log.info("+ Current user email: {}", currentUserEmail);
        Optional<UserEntity> userEntity = userService.getUserByEmail(currentUserEmail);
        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            Integer userId = user.getUserId();
            log.info("+ Current user ID: {}", userId);

            ProjectEntity projectEntity = projectService.getProjectsById(projectId);
            Integer projectOwnerId = projectEntity.getUserProject().getUserId();
            log.info("+ Project owner ID: {}", projectOwnerId);

            return projectOwnerId.equals(userId);
        }
        log.warn("+ User entity not found for email {}", currentUserEmail);
        return false;
    }
}
