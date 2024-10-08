package com.EffortlyTimeTracker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "task_tag")
public class TaskTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.annotation.Id             // Для MongoDB
    @org.springframework.data.mongodb.core.mapping.Field("_id")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskEntity task;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private TagEntity tag;

    // Поле для хранения идентификатора пользователя mongo
    @Column(name = "task_id", insertable = false, updatable = false)
    private Integer taskId;

    @Column(name = "tag_id", insertable = false, updatable = false)
    private Integer tagId;
}

