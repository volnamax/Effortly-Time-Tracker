package com.EffortlyTimeTracker.entity;

import com.EffortlyTimeTracker.converter.RoleConverter;
import com.EffortlyTimeTracker.enums.Role;
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
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
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

    @Convert(converter = RoleConverter.class)
    @Column(name = "role")
    private Role role;




    //cascade = CascadeType.ALL указывает, что все операции, включая удаление, распространяются с пользователя на его проекты.
    // orphanRemoval = true означает, что любой проект, который больше не ассоциирован с пользователем, будет автоматически удален.
//    @JsonManagedReference
//    @OneToMany(mappedBy = "userProject", cascade = CascadeType.ALL, orphanRemoval = true)
//    List<Project> projects;
//
//    @JsonManagedReference
//    @OneToMany(mappedBy = "userTodo", cascade = CascadeType.ALL, orphanRemoval = true)
//    List<TodoNode> todoNodes;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    @JsonIgnoreProperties("user")
//    private Set<GroupUser> userGroups = new HashSet<>();
}

