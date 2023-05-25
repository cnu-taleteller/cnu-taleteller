package com.cnu.taleteller.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.context.request.RequestContextListener;

@EnableJpaAuditing //JPA Auditing 활성화(등록/수정일 관련)
@SpringBootApplication
@EnableAsync
public class BackendApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
