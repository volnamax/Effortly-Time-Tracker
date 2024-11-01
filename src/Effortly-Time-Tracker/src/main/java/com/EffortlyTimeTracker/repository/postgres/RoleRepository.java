package com.EffortlyTimeTracker.repository.postgres;

import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.enums.Role;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("postgres")
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findByName(Role name);

}