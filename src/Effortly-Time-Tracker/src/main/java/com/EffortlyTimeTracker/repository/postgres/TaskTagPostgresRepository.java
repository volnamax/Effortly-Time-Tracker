package com.EffortlyTimeTracker.repository.postgres;

import com.EffortlyTimeTracker.entity.TaskTagEntity;
import com.EffortlyTimeTracker.repository.ITaskTagRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("postgres")
public interface TaskTagPostgresRepository extends JpaRepository<TaskTagEntity, Integer>, ITaskTagRepository {
}
