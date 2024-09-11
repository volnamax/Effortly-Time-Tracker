package com.EffortlyTimeTracker.repository.mongo;

import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.repository.ITodoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("mongo")// Переименовываем бин для Mongo
public interface TodoMongoRepository extends MongoRepository<TodoNodeEntity, Integer>, ITodoRepository {
    @Override
    List<TodoNodeEntity> findByUserId(Integer userId); // MongoDB automatically handles this based on method name
}