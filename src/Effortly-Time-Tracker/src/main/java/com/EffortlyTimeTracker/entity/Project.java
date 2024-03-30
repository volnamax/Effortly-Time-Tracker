package com.EffortlyTimeTracker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "projects")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column
    String name;

    @Column
    String description;

    @OneToMany(mappedBy = "project")
    List<ProjectTable> tables;

    @OneToMany(mappedBy = "project")
    List<ProjectTag> tags;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}


