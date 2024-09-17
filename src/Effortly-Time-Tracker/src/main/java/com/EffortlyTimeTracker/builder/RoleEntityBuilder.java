package com.EffortlyTimeTracker.builder;

import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.enums.Role;

public class RoleEntityBuilder {
    private Integer id = 1;
    private Role name = Role.USER;

    public RoleEntityBuilder() {
    }

    public RoleEntityBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public RoleEntityBuilder withRole(Role role) {
        this.name = role;
        return this;
    }

    public RoleEntity build() {
        return new RoleEntity(id, name);
    }
}
