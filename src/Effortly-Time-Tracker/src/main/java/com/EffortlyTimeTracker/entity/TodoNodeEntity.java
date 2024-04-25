package com.EffortlyTimeTracker.entity;

import com.EffortlyTimeTracker.enums.Priority;
import com.EffortlyTimeTracker.enums.Status;
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
public class TodoNodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_todo")
    Integer todoId;

    @Column(name = "content", nullable = true)
    String content;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(name = "due_data", nullable = true)
    LocalDateTime dueData;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonBackReference
    UserEntity user;
}
