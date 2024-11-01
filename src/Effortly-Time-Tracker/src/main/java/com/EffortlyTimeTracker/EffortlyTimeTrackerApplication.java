package com.EffortlyTimeTracker;

import org.hibernate.boot.internal.SessionFactoryOptionsBuilder;
import org.hibernate.engine.config.internal.ConfigurationServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude = {
		MongoAutoConfiguration.class,
		MongoDataAutoConfiguration.class
})
public class EffortlyTimeTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EffortlyTimeTrackerApplication.class, args);
	}

}
