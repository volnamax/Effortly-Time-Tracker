package com.EffortlyTimeTracker.repository;


import com.EffortlyTimeTracker.entity.TagEntity;
import com.EffortlyTimeTracker.entity.TaskTagEntity;

import java.util.List;
import java.util.Optional;


public interface ITaskTagRepository {
    // CRUD operations
    TaskTagEntity save(TaskTagEntity taskTag);
    void deleteById(Integer id);
    Optional<TaskTagEntity> findById(Integer id);
    boolean existsById(Integer id);
    List<TaskTagEntity> findAll();

    //todo  Ensure deleteAll method is available todo
    void deleteAll(Iterable<? extends TaskTagEntity> entities);
}