package com.EffortlyTimeTracker.entity;

import com.EffortlyTimeTracker.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private Role name;

    // Конструктор по умолчанию (если необходим для JPA)
    public RoleEntity() {
    }

    // Конструктор с параметрами
    public RoleEntity(Integer id, Role name) {
        this.id = id;
        this.name = name;
    }

    // Геттеры и сеттеры
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Role getName() {
        return name;
    }

    public void setName(Role name) {
        this.name = name;
    }
}
