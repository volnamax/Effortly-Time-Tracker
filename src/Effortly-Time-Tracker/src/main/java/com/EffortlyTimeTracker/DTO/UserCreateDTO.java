package com.EffortlyTimeTracker.DTO;

import com.EffortlyTimeTracker.enums.Role;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateDTO {
    @NotBlank(message = "Error user name is empty")
    String userName;

    @NotBlank(message = "Error user second name is empty")
    String userSecondname;

    @NotBlank(message = "Error user email is empty")
    @Email(message = "Error user email is invalid")
    String email;

    @NotBlank(message = "Error user password is empty")
    String passwordHash;

    @NotBlank(message = "Error user role is empty")
    String role;

    LocalDateTime dataSignIn;
}
