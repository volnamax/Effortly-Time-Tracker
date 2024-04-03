package com.EffortlyTimeTracker.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "TagProject")
public class TagProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer tagId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "color", nullable = true)
    String color;

    // Связь с проектом
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    Project project;

    @ManyToMany(mappedBy = "tags")
    Set<TaskTable> tasks;


}