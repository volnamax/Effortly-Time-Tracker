package com.EffortlyTimeTracker.entity;


import com.EffortlyTimeTracker.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "taskId")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_task")
    Integer taskId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", nullable = true)
    String description;

    // todo enums to one file
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

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
    @JoinColumn(name = "table_id")
//    @JsonBackReference
    TableEntity table;


//    // @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @ManyToMany
//    @JoinTable(
//            name = "task_tag", // Correct name of the join table
//            joinColumns = @JoinColumn(name = "taskId", referencedColumnName = "taskId"), // Link to TaskTable
//            inverseJoinColumns = @JoinColumn(name = "tagId", referencedColumnName = "tagId") // Link to TagProject
//    )
//    private Set<TagProject> tags;

}

