package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {

}
