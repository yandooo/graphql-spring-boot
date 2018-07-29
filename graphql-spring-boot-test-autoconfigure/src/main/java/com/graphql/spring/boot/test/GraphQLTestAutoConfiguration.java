package com.graphql.spring.boot.test;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Profile("test")
//@ConditionalOnClass(TestRestTemplate.class)
public class GraphQLTestAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GraphQLTestUtils graphQLTestUtils() {
        return new GraphQLTestUtils();
    }

}
