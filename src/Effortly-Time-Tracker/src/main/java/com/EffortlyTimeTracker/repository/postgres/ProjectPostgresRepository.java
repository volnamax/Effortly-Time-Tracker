package com.EffortlyTimeTracker.repository.postgres;

import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.repository.IProjectRepository;
import com.EffortlyTimeTracker.repository.ITodoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Profile("postgres")
public interface ProjectPostgresRepository extends JpaRepository<ProjectEntity, Integer>, IProjectRepository {
    @Query("SELECT t FROM ProjectEntity t WHERE t.userProject.userId = :userId")
    List<ProjectEntity> findByUserId(@Param("userId") Integer userId);
}
