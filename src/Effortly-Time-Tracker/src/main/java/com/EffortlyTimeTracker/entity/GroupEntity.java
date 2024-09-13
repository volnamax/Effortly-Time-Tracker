package com.EffortlyTimeTracker.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.Indexed;

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

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GroupMermberEntity> members = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id_project")
    private ProjectEntity project;

    // Добавляем поле userId для MongoDB (индексируемое)
    @Indexed(unique = true)
    @Transient  // Транзитное для JPA, но используемое MongoDB
    private Integer projectId;

    @Override
    public String toString() {
        return "GroupEntity{" + "groupId=" + groupId + ", name='" + name + '\'' + ", description='" + description + '\'' + ", project=" + project.getProjectId() + '}';
    }
}