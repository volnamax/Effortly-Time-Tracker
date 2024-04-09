package com.EffortlyTimeTracker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "projectId")
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
//    @JsonBackReference
    UserEntity userProject;

    @OneToOne
    @JoinColumn(name = "group_id", unique = true)
    private GroupEntity group;

//    @JsonManagedReference
//    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
//    List<TableProject> tables;
//
//    @JsonManagedReference
//    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
//    List<TagProject> tags;


//    @OneToOne(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private Group group;

}


