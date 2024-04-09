package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<TodoNodeEntity, Integer> {
}
