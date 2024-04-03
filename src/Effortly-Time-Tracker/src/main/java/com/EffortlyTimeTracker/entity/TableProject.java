package com.EffortlyTimeTracker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "TableProject")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer tableId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", nullable = true)
    String description;

    @Enumerated(EnumType.STRING)
    private TableProject.Status status;

    public enum Status {
        NO_ACTIVE, ACTIVE
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    Project project;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
    List<TaskTable> tasks;
}
