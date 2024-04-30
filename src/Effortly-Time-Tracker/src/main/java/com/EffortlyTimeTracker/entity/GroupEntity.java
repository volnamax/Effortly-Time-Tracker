// todo del fatch = lazy
// todo del dto for service
// todo check to all entity for controller and service


package com.EffortlyTimeTracker.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "group_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer groupId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", nullable = true)
    String description;

    @OneToMany(mappedBy = "group")
    private Set<GroupMermberEntity> members = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id_project")
    private ProjectEntity project;

    @Override
    public String toString() {
        return "GroupEntity{" +
                "groupId=" + groupId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", project=" + project.getProjectId() +
                '}';
    }
}