package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.TodoNode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<TodoNode, Integer> {
}
