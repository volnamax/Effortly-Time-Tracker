package com.EffortlyTimeTracker.DTO;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDTO {
    Long userId;
    String userName;
    String userSecondname;
    String email;
    private String role;
    LocalDateTime dataSignIn;
    LocalDateTime dataLastLogin;
}
