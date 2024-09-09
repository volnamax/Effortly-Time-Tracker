package com.EffortlyTimeTracker.repository;


import com.EffortlyTimeTracker.entity.TagEntity;

import java.util.List;
import java.util.Optional;


public interface ITaskTagRepository {
    // CRUD operations
    TagEntity save(TagEntity tagEntity);
    void deleteById(Integer id);
    Optional<TagEntity> findById(Integer id);
    boolean existsById(Integer id);
    List<TagEntity> findAll();

    //todo  Ensure deleteAll method is available todo
    void deleteAll(Iterable<? extends TagEntity> entities);
}