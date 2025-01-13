package com.EffortlyTimeTracker.unit.middlewareOwn.todo;


import com.EffortlyTimeTracker.unit.middlewareOwn.OwnerOperation;
import com.EffortlyTimeTracker.DTO.todo.TodoNodeDTO;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.unit.TodoService;
import com.EffortlyTimeTracker.unit.UserService;
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
public class OwnershipTodo {
    private final UserService userService;
    private final TodoService todoService;
    private final OwnerOperation ownerOperation;



    @Autowired
    public OwnershipTodo(@NonNull UserService userService, TodoService todoService, OwnerOperation ownerOperation) {
        this.userService = userService;
        this.todoService = todoService;
        this.ownerOperation = ownerOperation;
    }


    @Before("@annotation(CheckOwner) && args(userId,..)")
    public void checkOwnership(JoinPoint joinPoint, Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ownerOperation.isAdmin(authentication) || ownerOperation.isCurrentUser(authentication, userId, userService)) {
            // User is authorized;
            ;
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    @Before("@annotation(CheckTaskOwner) && args(taskId,..)")
    public void checkTaskOwnership(JoinPoint joinPoint, Integer taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ownerOperation.isAdmin(authentication) || isTaskOwner(authentication, taskId)) {
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
            if (!userId.equals(todoNodeDTO.getUserID()) && (!ownerOperation.isAdmin(authentication))) {
                throw new AccessDeniedException("You are not authorized to add a todo for this user");
            }
        } else {
            throw new AccessDeniedException("User not found");
        }
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

