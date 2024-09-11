package com.EffortlyTimeTracker.config;

import com.EffortlyTimeTracker.repository.*;
import com.EffortlyTimeTracker.repository.mongo.*;
import com.EffortlyTimeTracker.repository.postgres.*;
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
    public IProjectRepository projectPostgresRepository(ProjectPostgresRepository repository) {
        return repository;
    }

    @Bean
    @Profile("mongo")
    public IProjectRepository projectMongoRepository(ProjectMongoRepository repository) {
        return repository;
    }


    @Bean
    @Profile("postgres")
    public ITableRepository tablePostgresRepository(TablePostgresRepository repository) {
        return repository;
    }

    @Bean
    @Profile("mongo")
    public ITableRepository tableMongoRepository(TableMongoRepository repository) {
        return repository;
    }

    @Bean
    @Profile("postgres")
    public ITaskRepository taskPostgresRepository(TaskPostgresRepository repository) {
        return repository;
    }

    @Bean
    @Profile("mongo")
    public ITaskRepository taskMongoRepository(TaskMongoRepository repository) {
        return repository;
    }

    @Bean
    @Profile("postgres")
    public ITagRepository tagPostgresRepository(TagPostgresRepository repository) {
        return repository;
    }

    @Bean
    @Profile("mongo")
    public ITagRepository tagMongoRepository(TagMongoRepository repository) {
        return repository;
    }

    @Bean
    @Profile("postgres")
    public ITaskTagRepository taskTagPostgresRepository(TaskTagPostgresRepository repository) {
        return repository;
    }

    @Bean
    @Profile("mongo")
    public ITaskTagRepository taskTagMongoRepository(TaskTagMongoRepository repository) {
        return repository;
    }
}
