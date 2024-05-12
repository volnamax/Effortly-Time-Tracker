package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.GroupMermberEntity;
import com.EffortlyTimeTracker.entity.TaskTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTagRepository extends JpaRepository<TaskTagEntity, Integer> {
}
