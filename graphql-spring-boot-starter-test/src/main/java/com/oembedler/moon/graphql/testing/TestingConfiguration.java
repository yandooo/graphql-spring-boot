package com.oembedler.moon.graphql.testing;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
//@Profile("test")
//@ConditionalOnClass(TestRestTemplate.class)
public class TestingConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GraphQLTestUtils graphQLTestUtils() {
        return new GraphQLTestUtils();
    }

}
