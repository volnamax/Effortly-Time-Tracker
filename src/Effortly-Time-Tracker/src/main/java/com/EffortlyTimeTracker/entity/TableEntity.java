package com.EffortlyTimeTracker.entity;

import com.EffortlyTimeTracker.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "table_app")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_table")
    Integer tableId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", nullable = true)
    String description;

    @Enumerated(EnumType.STRING)
    com.EffortlyTimeTracker.enums.Status status;


    @ManyToOne()
    @JoinColumn(name = "project_id")
    @JsonBackReference
    ProjectEntity project;

//
//    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
//    List<TaskTable> tasks;


}
