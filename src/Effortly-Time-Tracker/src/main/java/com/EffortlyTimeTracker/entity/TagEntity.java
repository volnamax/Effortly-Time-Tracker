package com.EffortlyTimeTracker.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
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
    @org.springframework.data.annotation.Id             // Для MongoDB
    @org.springframework.data.mongodb.core.mapping.Field("_id")
    Integer tagId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "color", nullable = true)
    String color;

    @ManyToOne()
    @JoinColumn(name = "project_id")
    @JsonBackReference
    ProjectEntity project;

    // Поле для хранения идентификатора пользователя mongo
    @Column(name = "project_id", insertable = false, updatable = false)
    private Integer projectId;

//    @ManyToMany(mappedBy = "tag")
//    Set<TaskEntity> tasks;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskTagEntity> tasks = new HashSet<>();

    @Override
    public String toString() {
        return "TagEntity{" +
                "tagId=" + tagId +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", project=" + project +
                ", tasks=" + tasks +
                '}';
    }
}