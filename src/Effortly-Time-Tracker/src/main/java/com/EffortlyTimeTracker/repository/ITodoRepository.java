package com.EffortlyTimeTracker.repository;


import com.EffortlyTimeTracker.entity.TodoNodeEntity;

import java.util.List;
import java.util.Optional;


public interface ITodoRepository {
    // Find todos by userId
    List<TodoNodeEntity> findByUserId(Integer userId);

    // CRUD operations
    TodoNodeEntity save(TodoNodeEntity todoNodeEntity);
    void deleteById(Integer id);
    Optional<TodoNodeEntity> findById(Integer id);
    boolean existsById(Integer id);
    List<TodoNodeEntity> findAll();

    // Ensure deleteAll method is available
    void deleteAll(Iterable<? extends TodoNodeEntity> entities);
}