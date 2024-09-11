package com.EffortlyTimeTracker.repository.postgres;

import com.EffortlyTimeTracker.entity.TagEntity;
import com.EffortlyTimeTracker.repository.ITagRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("postgres")
public interface TagPostgresRepository extends JpaRepository<TagEntity, Integer>, ITagRepository {
}
