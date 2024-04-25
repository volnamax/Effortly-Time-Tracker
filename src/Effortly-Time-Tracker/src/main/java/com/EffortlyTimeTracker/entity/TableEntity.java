package com.EffortlyTimeTracker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "table")
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
    private Status status;
    public enum Status {
        NO_ACTIVE, ACTIVE
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
//    @JsonBackReference
    ProjectEntity project;

//
//    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
//    List<TaskTable> tasks;


}
