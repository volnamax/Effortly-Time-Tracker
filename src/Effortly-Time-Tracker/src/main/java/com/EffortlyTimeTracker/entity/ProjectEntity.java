package com.EffortlyTimeTracker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_project")
    @org.springframework.data.annotation.Id             // Для MongoDB
    @org.springframework.data.mongodb.core.mapping.Field("_id")
    Integer projectId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", nullable = true)
    String description;

    // Связь с пользователем
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    UserEntity userProject;
    // Поле для хранения идентификатора пользователя
    @Column(name = "user_id", insertable = false, updatable = false)
    private Integer userId;
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    GroupEntity group;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<TableEntity> tables;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    Set<TagEntity> tags;

    @Override
    public String toString() {
        return "ProjectEntity{" +
                "projectId=" + projectId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", userProject=" + (userProject != null ? userProject.getUserId() : "null") + '\'' +
                ", group=" + group +
                '}';
    }

}


