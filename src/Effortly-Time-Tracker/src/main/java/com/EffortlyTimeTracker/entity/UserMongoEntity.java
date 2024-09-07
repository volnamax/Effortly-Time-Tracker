package com.EffortlyTimeTracker.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserMongoEntity {
    @Id
    String id;
    String userName;
    String userSecondname;
    String email;
    String passwordHash;
    String role;
    LocalDateTime dataSignIn;
    LocalDateTime dataLastLogin;
}
