package com.graphql.spring.boot.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(value = "graphql.servlet.enabled", havingValue = "true", matchIfMissing = true)
public class GraphQLTestAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GraphQLTestTemplate graphQLTestUtils() {
        return new GraphQLTestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLTestSubscription graphQLTestSubscription(
        final Environment environment,
        final ObjectMapper objectMapper,
        @Value("${graphql.servlet.subscriptions.websocket.path:subscriptions}")
        final String subscriptionPath
    ) {
        return new GraphQLTestSubscription(environment, objectMapper, subscriptionPath);
    }

}
