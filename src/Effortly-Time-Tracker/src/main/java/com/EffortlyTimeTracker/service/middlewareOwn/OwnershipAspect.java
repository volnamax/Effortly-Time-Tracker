package com.EffortlyTimeTracker.service.middlewareOwn;


import com.EffortlyTimeTracker.DTO.todo.TodoNodeDTO;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.repository.RoleRepository;
import com.EffortlyTimeTracker.repository.UserRepository;
import com.EffortlyTimeTracker.service.TodoService;
import com.EffortlyTimeTracker.service.UserService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
@Slf4j
public class OwnershipAspect {
    private final UserService userService;
    private final TodoService todoService;


    @Autowired
    public OwnershipAspect(@NonNull UserService userService, TodoService todoService) {
        this.userService = userService;
        this.todoService = todoService;
    }


    @Before("@annotation(CheckOwner) && args(userId,..)")
    public void checkOwnership(JoinPoint joinPoint, Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authentication) || isCurrentUser(authentication, userId)) {
            // User is authorized;
            ;
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    @Before("@annotation(CheckTaskOwner) && args(taskId,..)")
    public void checkTaskOwnership(JoinPoint joinPoint, Integer taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authentication) || isTaskOwner(authentication, taskId)) {
            // User is authorized
        } else {
            throw new AccessDeniedException("You are not authorized to access this task");
        }
    }


    @Before("@annotation(CheckUserIdMatchesCurrentUser) && args(todoNodeDTO,..)")
    public void checkUserIdMatchesCurrentUser(JoinPoint joinPoint, TodoNodeDTO todoNodeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            throw new AccessDeniedException("You are not authorized to add a todo");
        }
        String currentUserEmail = authentication.getName();
        Optional<UserEntity> userEntity = userService.getUserByEmail(currentUserEmail);
        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            Integer userId = user.getUserId();
            if (!userId.equals(todoNodeDTO.getUserID()) && (!isAdmin(authentication))) {
                throw new AccessDeniedException("You are not authorized to add a todo for this user");
            }
        } else {
            throw new AccessDeniedException("User not found");
        }
    }


    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }


    private boolean isCurrentUser(Authentication authentication, Integer userId) {
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

    private boolean isTaskOwner(Authentication authentication, Integer taskId) {
        log.info("+ Checking owner of task " + taskId);

        String currentUserEmail = authentication.getName();
        log.info("+ Current user email: {}", currentUserEmail);
        Optional<UserEntity> userEntity = userService.getUserByEmail(currentUserEmail);
        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            Integer userId = user.getUserId();

            TodoNodeEntity taskEntity = todoService.getTodoById(taskId);
            Integer taskOwnerId = taskEntity.getUser().getUserId();

            log.info("+ Task owner id: {}", taskOwnerId);
            return taskOwnerId.equals(userId);

        }
        return false;
    }
}

