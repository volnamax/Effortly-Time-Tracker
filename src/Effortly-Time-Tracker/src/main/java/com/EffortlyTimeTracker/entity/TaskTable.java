package com.EffortlyTimeTracker.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "TaskTable")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer taskId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", nullable = true)
    String description;

    @Column(name = "status", nullable = false)
    String status;

    @Column(name = "sum_timer", nullable = true)
    Long sumTimer;

    @Column(name = "start_timer", nullable = true)
    LocalDateTime startTimer;

    @Column(name = "time_add_task", nullable = true)
    LocalDateTime timeAddTask;

    @Column(name = "time_end_task", nullable = true)
    LocalDateTime timeEndTask;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userId", nullable = false)
//    private User createdBy;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tableId")
    TableProject table;


    // @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ManyToMany
    @JoinTable(
            name = "task_tag", // Correct name of the join table
            joinColumns = @JoinColumn(name = "taskId", referencedColumnName = "taskId"), // Link to TaskTable
            inverseJoinColumns = @JoinColumn(name = "tagId", referencedColumnName = "tagId") // Link to TagProject
    )
    private Set<TagProject> tags;

}

