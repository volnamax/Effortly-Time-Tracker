package com.EffortlyTimeTracker.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "task")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column
    String name;

    @Column
    String description;

    @Column
    String status;

    @Column(name = "sum_timer")
    Long sumTimer;

    @Column(name = "start_timer")
    LocalDateTime startTimer;

    @Column(name = "time_add_task")
    LocalDateTime timeAddTask;

    @Column(name = "time_end_task")
    LocalDateTime timeEndTask;

    @ManyToMany
    @JoinTable(
            name = "task_tags",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    List<ProjectTag> tags;
}

