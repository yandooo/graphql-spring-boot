package com.graphql.spring.boot.test;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(DispatcherServlet.class)
@ConditionalOnProperty(value = "graphql.servlet.enabled", havingValue = "true", matchIfMissing = true)
public class GraphQLTestAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GraphQLTestTemplate graphQLTestUtils() {
        return new GraphQLTestTemplate();
    }

}
