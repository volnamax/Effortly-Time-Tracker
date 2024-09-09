package com.EffortlyTimeTracker.config;

import com.EffortlyTimeTracker.repository.IProjectRepository;
import com.EffortlyTimeTracker.repository.ITodoRepository;
import com.EffortlyTimeTracker.repository.IUserRepository;
import com.EffortlyTimeTracker.repository.mongo.ProjectMongoRepository;
import com.EffortlyTimeTracker.repository.mongo.TodoMongoRepository;
import com.EffortlyTimeTracker.repository.postgres.ProjectPostgresRepository;
import com.EffortlyTimeTracker.repository.postgres.TodoPostgresRepository;
import com.EffortlyTimeTracker.repository.postgres.UserPostgresRepository;
import com.EffortlyTimeTracker.repository.mongo.UserMongoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class RepositoryConfig {

    @Bean
    @Profile("postgres")
    public IUserRepository userPostgresRepository(UserPostgresRepository repository) {
        return repository;
    }

    @Bean
    @Profile("mongo")
    public IUserRepository userMongoRepository(UserMongoRepository repository) {
        return repository;
    }

    // PostgreSQL repository for TodoNodeEntity
    @Bean
    @Profile("postgres")
    public ITodoRepository todoPostgresRepository(TodoPostgresRepository repository) {
        return repository;
    }

    // MongoDB repository for TodoNodeEntity
    @Bean
    @Profile("mongo")
    public ITodoRepository todoMongoRepository(TodoMongoRepository repository) {
        return repository;
    }

    @Bean
    @Profile("postgres")
    public IProjectRepository projectPostgresRepository(ProjectPostgresRepository repository) {return  repository;}

    @Bean
    @Profile("mongo")
    public IProjectRepository projectMongoRepository(ProjectMongoRepository repository) {return repository;}
}
