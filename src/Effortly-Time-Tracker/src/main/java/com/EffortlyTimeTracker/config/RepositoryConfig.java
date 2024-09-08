package com.EffortlyTimeTracker.config;

import com.EffortlyTimeTracker.repository.IUserRepository;
import com.EffortlyTimeTracker.repository.UserPostgresRepository;
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
}
