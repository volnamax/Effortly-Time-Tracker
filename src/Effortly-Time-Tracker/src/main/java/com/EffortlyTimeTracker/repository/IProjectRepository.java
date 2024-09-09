package com.EffortlyTimeTracker.repository;


import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;

import java.util.List;
import java.util.Optional;


public interface IProjectRepository {
    // Find todos by userId
    List<ProjectEntity> findByUserId(Integer userId);

    // CRUD operations
    ProjectEntity save(ProjectEntity projectEntity);
    void deleteById(Integer id);
    Optional<ProjectEntity> findById(Integer id);
    boolean existsById(Integer id);
    List<ProjectEntity> findAll();

    //todo  Ensure deleteAll method is available todo
    void deleteAll(Iterable<? extends ProjectEntity> entities);
}