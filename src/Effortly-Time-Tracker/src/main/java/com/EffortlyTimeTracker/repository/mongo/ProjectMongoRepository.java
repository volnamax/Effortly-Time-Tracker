package com.EffortlyTimeTracker.repository.mongo;

import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.repository.IProjectRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("mongo")// Переименовываем бин для Mongo
public interface ProjectMongoRepository extends MongoRepository<ProjectEntity, Integer>, IProjectRepository {
    @Override
    List<ProjectEntity> findByUserId(Integer userId); // MongoDB automatically handles this based on method name
}