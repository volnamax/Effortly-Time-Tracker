package com.EffortlyTimeTracker.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tag")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tagId")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tags")
    Integer tagId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "color", nullable = true)
    String color;

    // Связь с проектом
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
//    @JsonBackReference
            ProjectEntity project;
//
//    @ManyToMany(mappedBy = "tags")
//    Set<TaskTable> tasks;
}