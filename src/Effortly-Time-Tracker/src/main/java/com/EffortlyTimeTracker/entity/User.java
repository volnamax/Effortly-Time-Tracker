package com.EffortlyTimeTracker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id_user;

    @Column(name = "user_name")
    String userName;

    @Column(name = "user_secondname")
    String userSecondname;

    @Column
    String email;

    @Column
    String description;

    @Column(name = "data_sign_in")
    LocalDateTime dataSignIn;

    @Column(name = "data_last_login")
    LocalDateTime dataLastLogin;

    @Column
    @Enumerated(EnumType.STRING)
    Role role;

    @OneToMany(mappedBy = "user")
    private List<Project> projects;

    @OneToMany(mappedBy = "user")
    private List<TodoList> todoLists;

    @Override
    public String toString() {
        return "User{" +
                "id_user=" + id_user +
                ", user_name='" + userName + '\'' +
                ", user_secondname='" + userSecondname + '\'' +
                '}';
    }
}
