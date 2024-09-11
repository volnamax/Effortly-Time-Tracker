package com.EffortlyTimeTracker.repository.postgres;

import com.EffortlyTimeTracker.entity.GroupMermberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMermberEntity, Integer> {
}
