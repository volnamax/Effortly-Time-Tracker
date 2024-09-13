package com.EffortlyTimeTracker.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "task")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_task")
    @org.springframework.data.annotation.Id             // Для MongoDB
    @org.springframework.data.mongodb.core.mapping.Field("_id")
    Integer taskId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", nullable = true)
    String description;

    @Enumerated(EnumType.STRING)
    com.EffortlyTimeTracker.enums.Status status;

    @Column(name = "sum_timer", nullable = true)
    Long sumTimer;

    @Column(name = "start_timer", nullable = true)
    LocalDateTime startTimer;

    @Column(name = "time_add_task", nullable = true)
    LocalDateTime timeAddTask;

    @Column(name = "time_end_task", nullable = true)
    LocalDateTime timeEndTask;

    @ManyToOne()
    @JoinColumn(name = "table_id")
    @JsonBackReference
    TableEntity table;

    // Поле для хранения идентификатора пользователя mongo
    @Column(name = "table_id", insertable = false, updatable = false)
    private Integer tableId;

    private Integer projectId; // Связь с ProjectEntity

    @Builder.Default
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<TaskTagEntity> tasks = new HashSet<>();

    // @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @ManyToMany
//    @JoinTable(
//            name = "task_tag", // Correct name of the join table
//            joinColumns = @JoinColumn(name = "task_id"), // Link to Task     TablereferencedColumnName = "taskId"
//            inverseJoinColumns = @JoinColumn(name = "tag_id") // Link to TagProject
//    )
//    private Set<TagEntity> tags;

//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userId", nullable = false)
//    private User createdBy;


    @Override
    public String toString() {
        return "TaskEntity{" + "taskId=" + taskId + ", name='" + name + '\'' + ", description='" + description + '\'' + ", status=" + status + ", sumTimer=" + sumTimer + ", startTimer=" + startTimer + ", timeAddTask=" + timeAddTask + ", timeEndTask=" + timeEndTask + ", table=" + table + ", tasks=" + tasks + '}';
    }
}

