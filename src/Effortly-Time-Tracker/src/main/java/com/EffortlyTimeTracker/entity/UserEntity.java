package com.EffortlyTimeTracker.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_app")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    Integer userId;

    @Column(name = "user_name", nullable = false)
    String userName;

    @Column(name = "user_secondname", nullable = false)
    String userSecondname;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "data_sign_in", nullable = true)
    LocalDateTime dataSignIn;

    @Column(name = "data_last_log_in", nullable = true)
    LocalDateTime dataLastLogin;

    @Column(name = "password", nullable = false)
    String passwordHash;


    @ManyToOne()
    @JoinColumn(name = "role_id")
    RoleEntity role;

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userSecondname='" + userSecondname + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\''+
                ", role ='" + role.getName() + '\''+
                ", dataSignIn=" + dataSignIn + '\'' +
                ", dataLastLogin=" + dataLastLogin +'\''
                + "}";
    }
}

