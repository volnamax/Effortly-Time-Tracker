package com.EffortlyTimeTracker.service.middlewareOwn.table;

import com.EffortlyTimeTracker.DTO.table.TableCreateDTO;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.service.ProjectService;
import com.EffortlyTimeTracker.service.TableService;
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
public class OwnershipTable {
    private final UserService userService;
    private final TableService tableService;
    private final OwnerOperation ownerOperation;
    private final ProjectService projectService;

    @Autowired
    public OwnershipTable(@NonNull UserService userService, TableService tableService, OwnerOperation ownerOperation, ProjectService projectService) {
        this.userService = userService;
        this.tableService = tableService;
        this.ownerOperation = ownerOperation;
        this.projectService = projectService;

    }

    @Before("@annotation(CheckTableOwner) && args(tableId,..)")
    public void checkTableOwnership(JoinPoint joinPoint, Integer tableId) {
        log.info("+ Checking table ownership for table {}", tableId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ownerOperation.isAdmin(authentication) || isTableOwner(authentication, tableId)) {
            log.info("+ Authorization successful for table {}", tableId);
        } else {
            log.warn("+ Authorization failed for table {}", tableId);
            throw new AccessDeniedException("You are not authorized to access this table");
        }
    }

    @Before("@annotation(CheckUserIdMatchesCurrentUserTable) && args(tableDTO,..)")
    public void checkUserIdMatchesCurrentUserOwnership(JoinPoint joinPoint, TableCreateDTO tableDTO) {
        log.info("+ Checking proj id matches current user for table {}", tableDTO.getProjectId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            log.warn("+ User is not authenticated");
            throw new AccessDeniedException("You are not authorized to perform this action");
        }
        ProjectEntity project = projectService.getProjectsById(tableDTO.getProjectId());

        if (!ownerOperation.isCurrentUser(authentication, project.getUserProject().getUserId() , userService) && !ownerOperation.isAdmin(authentication)) {
            log.warn("+ User is not authorized to perform this action for userId {}", project.getUserProject().getUserId() );
            throw new AccessDeniedException("You are not authorized to perform this action for this user");
        }
        log.info("+ Authorization successful for userId {}", project.getUserProject().getUserId() );
    }

    private boolean isTableOwner(Authentication authentication, Integer tableId) {
        log.info("+ Checking if user is owner of table {}", tableId);
        String currentUserEmail = authentication.getName();
        log.info("+ Current user email: {}", currentUserEmail);
        Optional<UserEntity> userEntity = userService.getUserByEmail(currentUserEmail);
        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            Integer userId = user.getUserId();
            log.info("+ Current user ID: {}", userId);

            TableEntity tableEntity = tableService.getTableById(tableId);
            Integer tableOwnerId = tableEntity.getProject().getUserProject().getUserId();
            log.info("+ Table owner ID: {}", tableOwnerId);

            return tableOwnerId.equals(userId);
        }
        log.warn("+ User entity not found for email {}", currentUserEmail);
        return false;
    }
}
