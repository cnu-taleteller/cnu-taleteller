package com.cnu.taleteller.backend.global.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoTemplateConfig extends AbstractMongoClientConfiguration {

    @Value("${MONGO_NAME}")
    private String mongodbName;

    @Value("${MONGO_CLIENT}")
    private String mongodbClientName;

    @Override
    protected String getDatabaseName() {
        return mongodbName;
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(mongodbClientName);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
