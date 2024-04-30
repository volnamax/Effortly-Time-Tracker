package com.EffortlyTimeTracker.entity;
//todo fix tag task is not linked
//todo task tag

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "tag")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tags")
    Integer tagId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "color", nullable = true)
    String color;

    @ManyToOne()
    @JoinColumn(name = "project_id")
    @JsonBackReference
    ProjectEntity project;

    @ManyToMany(mappedBy = "tags")
    Set<TaskEntity> tasks;
}