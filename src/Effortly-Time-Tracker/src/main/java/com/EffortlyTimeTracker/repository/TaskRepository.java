package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.TaskTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskTable, Integer> {
}
