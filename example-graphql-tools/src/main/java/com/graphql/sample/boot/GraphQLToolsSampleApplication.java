package com.graphql.sample.boot;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import graphql.servlet.config.ObjectMapperConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GraphQLToolsSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphQLToolsSampleApplication.class, args);
    }

    @Bean
    public ObjectMapperConfigurer objectMapperConfigurer() {
        return (mapper -> mapper.registerModule(new JavaTimeModule()));
    }
}
