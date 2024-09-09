package com.EffortlyTimeTracker.repository.postgres;

import com.EffortlyTimeTracker.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

    @Query("SELECT t FROM TaskEntity t WHERE t.table.tableId = :tableId")
    List<TaskEntity> findByTableId(@Param("tableId") Integer tableId);

    @Query("SELECT t FROM TaskEntity t WHERE t.table.project.projectId = :projectId")
    List<TaskEntity> findByProjectId(@Param("projectId") Integer projectId);
}
