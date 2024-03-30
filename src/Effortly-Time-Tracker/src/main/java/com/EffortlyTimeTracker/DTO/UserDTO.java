package com.EffortlyTimeTracker.DTO;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE) // по умолсанию
public class UserDTO {
     String user_name;
     String user_secondname;
}
