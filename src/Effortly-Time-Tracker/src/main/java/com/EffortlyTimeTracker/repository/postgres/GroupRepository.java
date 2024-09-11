package com.EffortlyTimeTracker.repository.postgres;

import com.EffortlyTimeTracker.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupEntity, Integer> {
}
