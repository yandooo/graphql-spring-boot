package com.oembedler.moon.graphql.testing;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnBean(TestRestTemplate.class)
public class TestingConfiguration {

    @Bean
    public GraphQLTestUtils graphQLTestUtils() {
        return new GraphQLTestUtils();
    }

}
