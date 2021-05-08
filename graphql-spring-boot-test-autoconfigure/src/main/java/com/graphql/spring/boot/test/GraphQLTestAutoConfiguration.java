package com.graphql.spring.boot.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(
    value = "graphql.servlet.enabled",
    havingValue = "true",
    matchIfMissing = true)
public class GraphQLTestAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public GraphQLTestTemplate graphQLTestUtils(
      final ResourceLoader resourceLoader,
      @Autowired(required = false) final TestRestTemplate restTemplate,
      @Value("${graphql.servlet.mapping:/graphql}") final String graphqlMapping,
      final ObjectMapper objectMapper) {
    return new GraphQLTestTemplate(resourceLoader, restTemplate, graphqlMapping, objectMapper);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(ObjectMapper.class)
  public GraphQLTestSubscription graphQLTestSubscription(
      final Environment environment,
      final ObjectMapper objectMapper,
      @Value("${graphql.servlet.subscriptions.websocket.path:subscriptions}")
          final String subscriptionPath) {
    return new GraphQLTestSubscription(environment, objectMapper, subscriptionPath);
  }
}
