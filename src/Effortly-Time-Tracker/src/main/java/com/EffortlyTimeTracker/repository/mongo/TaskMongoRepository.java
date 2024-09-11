package com.EffortlyTimeTracker.repository.mongo;

import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.repository.ITableRepository;
import com.EffortlyTimeTracker.repository.ITaskRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("mongo")// Переименовываем бин для Mongo
public interface TaskMongoRepository extends MongoRepository<TaskEntity, Integer>, ITaskRepository {
    @Override
    List<TaskEntity> findByTableId(Integer tableId);

    @Override
    List<TaskEntity> findByProjectId(Integer projectId);

}