package com.EffortlyTimeTracker.service.middlewareOwn.group;

import com.EffortlyTimeTracker.DTO.group.AddUserToGroupDTO;
import com.EffortlyTimeTracker.DTO.group.GroupCreateDTO;
import com.EffortlyTimeTracker.entity.GroupEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.service.GroupService;
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
public class OwnershipGroup {
    private final UserService userService;
    private final GroupService groupService;
    private final OwnerOperation ownerOperation;
    private final ProjectService projectService;


    @Autowired
    public OwnershipGroup(@NonNull UserService userService, GroupService groupService, OwnerOperation ownerOperation, ProjectService projectService) {
        this.userService = userService;
        this.groupService = groupService;
        this.ownerOperation = ownerOperation;
        this.projectService = projectService;
    }

    @Before("@annotation(CheckProjectOwner) && args(groupCreateDTO,..)")
    public void checkProjectOwnership(JoinPoint joinPoint, GroupCreateDTO groupCreateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ownerOperation.isAdmin(authentication) || ownerOperation.isProjectOwner(authentication, groupCreateDTO.getProjectId(), userService, projectService)) {
            // User is authorized
        } else {
            throw new AccessDeniedException("You are not authorized to access this project");
        }
    }

    @Before("@annotation(CheckGroupOwner) && args(groupId,..)")
    public void checkGroupOwnership(JoinPoint joinPoint, Integer groupId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ownerOperation.isAdmin(authentication) || isGroupOwner(authentication, groupId)) {
            // User is authorized
        } else {
            throw new AccessDeniedException("You are not authorized to access this group");
        }
    }

    @Before("@annotation(CheckProjectOwner) && args(addUserToGroupDTO,..)")
    public void checkGroupOwnership(JoinPoint joinPoint, AddUserToGroupDTO addUserToGroupDTO) {
        Integer groupId = addUserToGroupDTO.getGroupId();
        log.info("+ Checking group ownership for group {}", groupId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ownerOperation.isAdmin(authentication) || isGroupOwner(authentication, groupId)) {
            log.info("+ Authorization successful for group {}", groupId);
        } else {
            log.warn("+ Authorization failed for group {}", groupId);
            throw new AccessDeniedException("You are not authorized to access this group");
        }
    }


    private boolean isGroupOwner(Authentication authentication, Integer groupId) {
        String currentUserEmail = authentication.getName();
        Optional<UserEntity> userEntity = userService.getUserByEmail(currentUserEmail);
        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            Integer userId = user.getUserId();

            GroupEntity groupEntity = groupService.getGroupById(groupId);
            Integer groupOwnerId = groupEntity.getProject().getUserProject().getUserId();

            return groupOwnerId.equals(userId);
        }
        return false;
    }


}

