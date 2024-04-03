package com.EffortlyTimeTracker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "AppUsers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer userId;

    @Column(name = "user_name", nullable = false)
    String userName;

    @Column(name = "user_secondname", nullable = false)
    String userSecondname;

    @Column(name = "email", nullable = false, unique = true)
    String email;
//todo nullable false
    @Column(name = "description", nullable = true)
    String description;

    @Column(name = "data_sign_in", nullable = true)
    LocalDateTime dataSignIn;

    @Column(name = "data_last_login", nullable = true)
    LocalDateTime dataLastLogin;

    @Enumerated(EnumType.STRING)
    private Role role;
    public enum Role {
        ADMIN, USER, GUEST
    }

    //cascade = CascadeType.ALL указывает, что все операции, включая удаление, распространяются с пользователя на его проекты.
    // orphanRemoval = true означает, что любой проект, который больше не ассоциирован с пользователем, будет автоматически удален.
    @OneToMany(mappedBy = "userProject", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Project> projects;

    @OneToMany(mappedBy = "userTodo", cascade = CascadeType.ALL, orphanRemoval = true)
    List<TodoNode> todoNodes;

    @ManyToMany(mappedBy = "usersGroup")
    private Set<Group> groupsUsers = new HashSet<>();

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userSecondname='" + userSecondname + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", dataSignIn=" + dataSignIn +
                ", dataLastLogin=" + dataLastLogin +
                ", role=" + role +
                ", projects=" + projects +
                ", todoLists=" + todoNodes +
                ", groupsUsers=" + groupsUsers +
                '}';
    }

}
