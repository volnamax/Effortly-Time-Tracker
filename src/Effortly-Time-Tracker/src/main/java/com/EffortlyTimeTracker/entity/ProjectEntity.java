package com.EffortlyTimeTracker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


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


    @OneToOne
    @JoinColumn(name = "group_id", unique = true)
    GroupEntity group;

//    @JsonManagedReference
//    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
//    List<TableEntity> tables;
//
//    @JsonManagedReference
//    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
//    List<TableEntity> tags;


//    @OneToOne(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JsonManagedReference
//    GroupEntity group;

    @Override
    public String toString() {
        return "ProjectEntity{" +
                "projectId=" + projectId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", userProject=" + userProject.getUserId() +
                ", group=" + group +
                '}';
    }
}


