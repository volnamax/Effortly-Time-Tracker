package com.EffortlyTimeTracker.repository;


import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ITaskRepository {
    List<TaskEntity> findByProjectId(Integer projectId);

    List<TaskEntity> findByTableId(Integer tableId);


    // CRUD operations
    TaskEntity save(TaskEntity tableEntity);

    void deleteById(Integer id);

    Optional<TaskEntity> findById(Integer id);

    boolean existsById(Integer id);

    List<TaskEntity> findAll();

    //todo  Ensure deleteAll method is available todo
    void deleteAll(Iterable<? extends TaskEntity> entities);
}