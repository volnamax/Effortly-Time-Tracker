package com.EffortlyTimeTracker.DTO.auth;

import com.EffortlyTimeTracker.DTO.userDTO.UserCreateDTO;

public record RegisterRequestDto(UserCreateDTO user,String login, String name, String password, String role) {
}
