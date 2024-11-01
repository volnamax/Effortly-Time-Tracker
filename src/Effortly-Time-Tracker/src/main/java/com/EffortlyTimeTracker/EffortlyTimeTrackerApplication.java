package com.EffortlyTimeTracker;

import org.hibernate.boot.internal.SessionFactoryOptionsBuilder;
import org.hibernate.engine.config.internal.ConfigurationServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.EffortlyTimeTracker.repository.postgres")
@EnableMongoRepositories(basePackages = "com.EffortlyTimeTracker.repository.mongo")
public class EffortlyTimeTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EffortlyTimeTrackerApplication.class, args);
	}

}
