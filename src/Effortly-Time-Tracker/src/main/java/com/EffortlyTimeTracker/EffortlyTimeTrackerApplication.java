package com.EffortlyTimeTracker;

import org.hibernate.boot.internal.SessionFactoryOptionsBuilder;
import org.hibernate.engine.config.internal.ConfigurationServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EffortlyTimeTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EffortlyTimeTrackerApplication.class, args);
	}

}
