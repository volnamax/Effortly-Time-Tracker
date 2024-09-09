package com.EffortlyTimeTracker.repository.mongo;

import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TaskTagEntity;
import com.EffortlyTimeTracker.repository.IProjectRepository;
import com.EffortlyTimeTracker.repository.ITaskTagRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("mongo")// Переименовываем бин для Mongo
public interface TaskTagMongoRepository extends MongoRepository<TaskTagEntity, Integer>, ITaskTagRepository {
}