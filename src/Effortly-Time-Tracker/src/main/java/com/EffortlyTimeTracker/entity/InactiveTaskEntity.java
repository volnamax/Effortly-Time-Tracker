package com.EffortlyTimeTracker.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "inactive_task")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InactiveTaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "task_id", nullable =  false)
    TaskEntity task;

    @Column(name = "deactivated_at", nullable = false)
    LocalDateTime deactivatedAt;

    @Column(name = "reason", nullable = true)
    String reason;
}
