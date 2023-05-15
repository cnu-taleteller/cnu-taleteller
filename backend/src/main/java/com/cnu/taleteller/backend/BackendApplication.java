package com.cnu.taleteller.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.context.request.RequestContextListener;

@EnableJpaAuditing
@SpringBootApplication
public class BackendApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}