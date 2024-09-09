package com.EffortlyTimeTracker.repository.mongo;

import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.repository.ITableRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("mongo")// Переименовываем бин для Mongo
public interface TableMongoRepository extends MongoRepository<TableEntity, Integer>, ITableRepository {
    @Override
    List<TableEntity> findByProjectId(Integer projectId);

}