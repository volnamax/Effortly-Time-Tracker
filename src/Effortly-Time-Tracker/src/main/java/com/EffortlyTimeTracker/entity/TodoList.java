package com.EffortlyTimeTracker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "Todolist")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TodoList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer todoId;

    @Column(name = "content", nullable = true)
    String content;


    @Enumerated(EnumType.STRING)
    private TodoList.Status status;

    public enum Status {
        NO_ACTIVE, ACTIVE
    }


    @Enumerated(EnumType.STRING)
    private TodoList.Priority priority;

    public enum Priority {
        IMPORTANT_URGENTLY, NO_IMPORTANT_URGENTLY, IMPORTANT_NO_URGENTLY, NO_IMPORTANT_NO_URGENTLY
    }


    @Column(name = "due_data", nullable = true)
    LocalDateTime dueData;

    // Связь с пользователем
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    User userTodo;
}
