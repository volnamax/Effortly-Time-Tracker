package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findByName(Role name);

}