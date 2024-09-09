package com.EffortlyTimeTracker.repository.mongo;

import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TagEntity;
import com.EffortlyTimeTracker.repository.IProjectRepository;
import com.EffortlyTimeTracker.repository.ITagRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("mongo")// Переименовываем бин для Mongo
public interface TagMongoRepository extends MongoRepository<TagEntity, Integer>, ITagRepository {
}