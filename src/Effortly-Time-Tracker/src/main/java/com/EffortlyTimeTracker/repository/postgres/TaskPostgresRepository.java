package com.EffortlyTimeTracker.repository.postgres;

import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.repository.ITaskRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("postgres")
public interface TaskPostgresRepository extends JpaRepository<TaskEntity, Integer>, ITaskRepository {

    @Query("SELECT t FROM TaskEntity t WHERE t.table.tableId = :tableId")
    List<TaskEntity> findByTableId(@Param("tableId") Integer tableId);

    @Query("SELECT t FROM TaskEntity t WHERE t.table.project.projectId = :projectId")
    List<TaskEntity> findByProjectId(@Param("projectId") Integer projectId);
}
