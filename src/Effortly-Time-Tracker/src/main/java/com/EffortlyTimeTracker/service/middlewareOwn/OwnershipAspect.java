package com.EffortlyTimeTracker.service.middlewareOwn;


import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.repository.RoleRepository;
import com.EffortlyTimeTracker.repository.UserRepository;
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


    @Autowired
    public OwnershipAspect(@NonNull UserService userService) {
        this.userService = userService;
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
            return userId == id;
        }
        return false;
    }
}

