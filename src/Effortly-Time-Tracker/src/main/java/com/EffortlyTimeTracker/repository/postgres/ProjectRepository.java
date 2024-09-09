package com.EffortlyTimeTracker.repository.postgres;

import com.EffortlyTimeTracker.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {
    @Query("SELECT t FROM ProjectEntity t WHERE t.userProject.userId = :userId")
    List<ProjectEntity> findByUserId(@Param("userId") Integer userId);

}
