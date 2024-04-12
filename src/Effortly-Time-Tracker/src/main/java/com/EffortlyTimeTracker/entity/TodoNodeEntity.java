package com.EffortlyTimeTracker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "todo_node")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "todoId")
public class TodoNodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_todo")
    Integer todoId;

    @Column(name = "content", nullable = true)
    String content;


    @Enumerated(EnumType.STRING)
    private Status status;
    public enum Status {
        NO_ACTIVE, ACTIVE
    }


    @Enumerated(EnumType.STRING)
    private TodoNodeEntity.Priority priority;

    public enum Priority {
        IMPORTANT_URGENTLY, NO_IMPORTANT_URGENTLY, IMPORTANT_NO_URGENTLY, NO_IMPORTANT_NO_URGENTLY
    }
//
//
//    @Column(name = "due_data", nullable = true)
//    LocalDateTime dueData;
//
//    // Связь с пользователем
//    @JoinColumn(name = "user_id", referencedColumnName = "id_user")
//    UserEntity userTodo;

}
