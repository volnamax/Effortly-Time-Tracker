package com.EffortlyTimeTracker.unit.middlewareOwn.tag;

import com.EffortlyTimeTracker.DTO.tag.TagCreateDTO;
import com.EffortlyTimeTracker.entity.*;
import com.EffortlyTimeTracker.unit.ProjectService;
import com.EffortlyTimeTracker.unit.TagService;
import com.EffortlyTimeTracker.unit.TaskService;
import com.EffortlyTimeTracker.unit.UserService;
import com.EffortlyTimeTracker.unit.middlewareOwn.OwnerOperation;
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
public class OwnershipTag {
    private final UserService userService;
    private final TagService tagService;
    private final OwnerOperation ownerOperation;
    private final ProjectService projectService;
    private final TaskService taskService;

    @Autowired
    public OwnershipTag(@NonNull UserService userService, TagService tagService, OwnerOperation ownerOperation, ProjectService projectService, TaskService taskService) {
        this.userService = userService;
        this.tagService = tagService;
        this.projectService = projectService;
        this.ownerOperation = ownerOperation;
        this.taskService = taskService;
    }

    @Before("@annotation(CheckTagOwner) && args(tagId,..)")
    public void checkTagOwnership(JoinPoint joinPoint, Integer tagId) {
        log.info("+ Checking tag ownership for tag {}", tagId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ownerOperation.isAdmin(authentication) || isTagOwner(authentication, tagId)) {
            log.info("+ Authorization successful for tag {}", tagId);
        } else {
            log.warn("+ Authorization failed for tag {}", tagId);
            throw new AccessDeniedException("You are not authorized to access this tag");
        }
    }

    @Before("@annotation(CheckUserIdMatchesCurrentUserTag) && args(tagDTO,..)")
    public void checkUserIdMatchesCurrentUserOwnership(JoinPoint joinPoint, TagCreateDTO tagDTO) {
        ProjectEntity project = projectService.getProjectsById(tagDTO.getProjectId());
        TaskEntity task = taskService.getTaskById(tagDTO.getTaskId());

        Integer idOwner = project.getUserProject().getUserId();
        Integer idTask = task.getTaskId();
        log.info("+ idTask = {}", idTask.toString());
        log.info("+ Checking userId matches current user for tag {}", idOwner);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            log.warn("+ User is not authenticated");
            throw new AccessDeniedException("You are not authorized to perform this action");
        }
        if (!ownerOperation.isTaskOwner(authentication, idTask, userService, taskService)) {
            log.warn("+ User is not authorized to perform this action for userId {}", idOwner);
            throw new AccessDeniedException("You are not authorized to perform this action for this user");
        }
        if (!ownerOperation.isCurrentUser(authentication, idOwner, userService) && !ownerOperation.isAdmin(authentication) ) {
            log.warn("+ User is not authorized to perform this action for userId {}", idOwner);
            throw new AccessDeniedException("You are not authorized to perform this action for this user");
        }
        log.info("+ Authorization successful for userId {}", idOwner);
    }

    private boolean isTagOwner(Authentication authentication, Integer tagId) {
        log.info("+ Checking if user is owner of tag {}", tagId);
        String currentUserEmail = authentication.getName();
        log.info("+ Current user email: {}", currentUserEmail);
        Optional<UserEntity> userEntity = userService.getUserByEmail(currentUserEmail);
        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            Integer userId = user.getUserId();
            log.info("+ Current user ID: {}", userId);

            TagEntity tagEntity = tagService.getTagkById(tagId);
            Integer tagOwnerId = tagEntity.getProject().getUserProject().getUserId();
            log.info("+ Tag owner ID: {}", tagOwnerId);

            return tagOwnerId.equals(userId);
        }
        log.warn("+ User entity not found for email {}", currentUserEmail);
        return false;
    }
}
