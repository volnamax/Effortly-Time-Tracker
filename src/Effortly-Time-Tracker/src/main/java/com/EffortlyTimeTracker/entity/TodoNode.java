package com.EffortlyTimeTracker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class TodoNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer todoId;

    @Column(name = "content", nullable = true)
    String content;


    @Enumerated(EnumType.STRING)
    private TodoNode.Status status;

    public enum Status {
        NO_ACTIVE, ACTIVE
    }


    @Enumerated(EnumType.STRING)
    private TodoNode.Priority priority;

    public enum Priority {
        IMPORTANT_URGENTLY, NO_IMPORTANT_URGENTLY, IMPORTANT_NO_URGENTLY, NO_IMPORTANT_NO_URGENTLY
    }


    @Column(name = "due_data", nullable = true)
    LocalDateTime dueData;

    // Связь с пользователем
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @JsonBackReference
    User userTodo;


//    @Override
//    public String toString() {
//        return "TodoNode{" +
//                "id=" + todoId +
//                ", content='" + content + '\'' +
//                ", status=" + status +
//                ", priority=" + priority +
//                ", dueDate=" + dueData +
//                ", userTodoId=" + (userTodo != null ? userTodo.getUserId() : null) +
//                '}';
//    }

}
