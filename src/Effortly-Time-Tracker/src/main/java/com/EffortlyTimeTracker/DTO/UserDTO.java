package com.EffortlyTimeTracker.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    @NotBlank(message = "Error user name is empty")
    String userName;

    @NotBlank(message = "Error user second name is empty")
    String userSecondname;

    @NotBlank(message = "Error user email is empty")
    @Email(message = "Error user email is invalid")
    String email;

    String description;
    String role;

    LocalDateTime dataSignIn;
    LocalDateTime dataLastLogin;
//    List<Project> projects;
//    List<TodoNode> todoNodes;
//
//    Set<Group> groupsUsers;
}
