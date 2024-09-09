package com.EffortlyTimeTracker.repository;


import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TableEntity;

import java.util.List;
import java.util.Optional;


public interface ITableRepository {
    // Find todos by userId
    List<TableEntity> findByProjectId(Integer projectId);

    // CRUD operations
    TableEntity save(TableEntity tableEntity);
    void deleteById(Integer id);
    Optional<TableEntity> findById(Integer id);
    boolean existsById(Integer id);
    List<TableEntity> findAll();

    void deleteAll(Iterable<? extends TableEntity> entities);
}