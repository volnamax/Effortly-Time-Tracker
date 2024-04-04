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
@Table(name = "Projects")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer projectId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", nullable = true)
    String description;

    // Связь с пользователем
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @JsonBackReference
    User userProject;

    @JsonManagedReference
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    List<TableProject> tables;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    List<TagProject> tags;

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    Group groupP;

//    @Override
//    public String toString() {
//        return "Project{" +
//                "projectId=" + projectId +
//                ", name='" + name + '\'' +
//                ", description='" + description + '\'' +
//                ", userProject=" + userProject +
//                ", tables=" + tables +
//                ", tags=" + tags +
//                ", groupP=" + groupP +
//                '}';
//    }
}


