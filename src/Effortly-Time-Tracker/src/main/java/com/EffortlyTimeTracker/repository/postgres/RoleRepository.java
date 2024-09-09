package com.EffortlyTimeTracker.repository.postgres;

import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findByName(Role name);

}