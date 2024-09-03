package com.EffortlyTimeTracker.security;

import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.service.TokenService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccessProviderImpl implements UserAccessProvider {

    private final TokenService tokenService;

    @Override
    public boolean checkId(Integer id) {
        var user = tokenService.getCurrentUser();
        return user.getUserId().equals(id) || user.getRole().getName().equals(Role.ADMIN);
    }

    @Override
    public boolean checkIfCreateAllowed(Integer id) {
        var user = tokenService.getCurrentUser();
        return user.getRole().getName().equals(Role.ADMIN);
    }

    @Override
    public boolean checkIfUpdateAllowed(Integer id) {
        return checkId(id);
    }

    @Override
    public boolean checkIfDeleteAllowed(Integer id) {
        return checkId(id);
    }

    @Override
    public boolean checkIfReadAllowed(Integer id) {
        return false;
    }
}
