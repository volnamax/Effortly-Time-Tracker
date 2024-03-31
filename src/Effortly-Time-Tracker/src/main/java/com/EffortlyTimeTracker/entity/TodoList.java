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

    @Column(name = "status", nullable = false)
    String status;

    @Column(name = "prioryty", nullable = true)
    String priority;

    @Column(name = "due_data", nullable = true)
    LocalDateTime dueData;

    // Связь с пользователем
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    User userTodo;
}
