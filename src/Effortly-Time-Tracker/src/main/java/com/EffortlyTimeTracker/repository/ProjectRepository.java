package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
