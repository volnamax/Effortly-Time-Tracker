package com.EffortlyTimeTracker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
    @org.springframework.data.annotation.Id             // Для MongoDB
    @org.springframework.data.mongodb.core.mapping.Field("_id")
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


    // Поле для хранения идентификатора пользователя mongo
    @Column(name = "project_id", insertable = false, updatable = false)
    private Integer projectId;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<TaskEntity> tasks;


    @Override
    public String toString() {
        return "TableEntity{" +
                "tableId=" + tableId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", project=" + (project != null ? project.getProjectId() : "null") + '\'' +
                '}';
    }
}
