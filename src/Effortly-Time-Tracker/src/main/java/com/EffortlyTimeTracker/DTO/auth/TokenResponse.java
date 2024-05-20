package com.EffortlyTimeTracker.DTO.auth;


import com.EffortlyTimeTracker.DTO.userDTO.UserResponseDTO;

public record TokenResponse(String token, UserResponseDTO user) { }
