package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.TagProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagProject, Integer> {

}
