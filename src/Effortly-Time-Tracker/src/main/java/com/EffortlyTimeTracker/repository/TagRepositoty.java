package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.TagProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepositoty extends JpaRepository<TagProject, Integer> {

}
