package com.EffortlyTimeTracker.repository.postgres;

import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.repository.ITableRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("postgres")
public interface TablePostgresRepository extends JpaRepository<TableEntity, Integer>, ITableRepository {
    @Query("SELECT t FROM TableEntity t WHERE t.project.projectId = :projectId")
    List<TableEntity> findByProjectId(@Param("projectId") Integer projectId);
}

