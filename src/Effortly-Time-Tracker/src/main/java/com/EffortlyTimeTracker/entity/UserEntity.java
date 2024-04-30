package com.EffortlyTimeTracker.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToMany(mappedBy ="user",  cascade = CascadeType.ALL,  orphanRemoval = true)
    @JsonManagedReference
    List<TodoNodeEntity> todoNodeEntityList;

    @OneToMany(mappedBy ="userProject",  cascade = CascadeType.ALL,  orphanRemoval = true)
    @JsonManagedReference
    List<ProjectEntity> projectEntities;

    @OneToMany(mappedBy = "user")
    Set<GroupMermberEntity> groups = new HashSet<>();

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userSecondname='" + userSecondname + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\''+
                ", role ='" + (role != null ? role.getName() : "null") + '\''+
                ", dataSignIn=" + dataSignIn + '\'' +
                ", dataLastLogin=" + dataLastLogin +'\''
                + "}";
    }
}

