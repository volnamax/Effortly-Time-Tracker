package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.TableProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<TableProject, Integer> {
}
