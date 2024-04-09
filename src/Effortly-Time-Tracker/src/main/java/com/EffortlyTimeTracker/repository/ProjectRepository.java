package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {
}
