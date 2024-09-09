package com.EffortlyTimeTracker.repository.postgres;

import com.EffortlyTimeTracker.entity.TaskTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTagPostgresRepository extends JpaRepository<TaskTagEntity, Integer> {
}
