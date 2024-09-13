package com.EffortlyTimeTracker.DTO.auth;


import com.EffortlyTimeTracker.DTO.user.UserResponseDTO;

public record TokenResponse(String token, UserResponseDTO user) {
}
