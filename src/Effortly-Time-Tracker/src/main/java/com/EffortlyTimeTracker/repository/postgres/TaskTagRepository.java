package com.EffortlyTimeTracker.repository.postgres;

import com.EffortlyTimeTracker.entity.TaskTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTagRepository extends JpaRepository<TaskTagEntity, Integer> {
}
