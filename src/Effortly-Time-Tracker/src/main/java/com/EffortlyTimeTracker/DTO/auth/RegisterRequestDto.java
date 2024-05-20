package com.EffortlyTimeTracker.DTO.auth;

import com.EffortlyTimeTracker.DTO.user.UserCreateDTO;

public record RegisterRequestDto(UserCreateDTO user,String login, String name, String password, String role) {
}
