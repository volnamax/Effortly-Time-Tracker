package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {
}
