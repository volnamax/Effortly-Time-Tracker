package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TableRepository extends JpaRepository<TableEntity, Integer> {
    @Query("SELECT t FROM TableEntity t WHERE t.project.projectId = :projectId")
    List<TableEntity> findByUserId(@Param("projectId") Integer projectId);
}
