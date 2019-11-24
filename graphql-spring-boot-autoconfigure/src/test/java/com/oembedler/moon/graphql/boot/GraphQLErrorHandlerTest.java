package com.oembedler.moon.graphql.boot;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.coxautodev.graphql.tools.SchemaParser;
import com.graphql.spring.boot.test.TestUtils;
import com.oembedler.moon.graphql.boot.error.ThrowableGraphQLError;
import com.oembedler.moon.graphql.boot.test.AbstractAutoConfigurationTest;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.kickstart.execution.GraphQLObjectMapper;
import graphql.kickstart.execution.error.GraphQLErrorHandler;
import graphql.schema.GraphQLSchema;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class GraphQLErrorHandlerTest extends AbstractAutoConfigurationTest {

  private GraphQL gql;
  private GraphQLObjectMapper objectMapper;

  public GraphQLErrorHandlerTest() {
    super(AnnotationConfigWebApplicationContext.class, GraphQLWebAutoConfiguration.class);
  }

  @Before
  public void setUp() {
    System.setProperty("graphql.tools.schemaLocationPattern", "graphql/error-handler-test.graphql");
    load(BaseConfiguration.class);

    GraphQLSchema schema = getContext().getBean(GraphQLSchema.class);
    gql = GraphQL.newGraphQL(schema).build();

    GraphQLErrorHandler errorHandler = getContext().getBean(GraphQLErrorHandler.class);
    objectMapper = GraphQLObjectMapper.newBuilder().withGraphQLErrorHandler(errorHandler).build();
  }

  @Test
  public void illegalArgumentExceptionShouldBeHandledConcretely() {
    TestUtils.assertGraphQLError(gql, "query { illegalArgumentException }",
            new ThrowableGraphQLError(new IllegalArgumentException("Illegal argument"), "Illegal argument"),
            objectMapper);
  }

  @Test
  public void illegalStateExceptionShouldBeHandledByCatchAll() {
    TestUtils.assertGraphQLError(gql, "query { illegalStateException }",
            new ThrowableGraphQLError(new IllegalStateException("Illegal state"), "Catch all handler"),
            objectMapper);
  }

  @Configuration
  static class BaseConfiguration {

    public class Query implements GraphQLQueryResolver {
      boolean illegalArgumentException() {
        throw new IllegalArgumentException("Illegal argument");
      }

      boolean illegalStateException() {
        throw new IllegalStateException("Illegal state");
      }

      @ExceptionHandler(IllegalArgumentException.class)
      ThrowableGraphQLError handle(IllegalArgumentException e) {
        return new ThrowableGraphQLError(e, "Illegal argument");
      }

      @ExceptionHandler(Throwable.class)
      GraphQLError handle(Throwable e) {
        return new ThrowableGraphQLError(e, "Catch all handler");
      }

      @Bean
      Query queryResolver() {
        return new Query();
      }

      @Bean
      GraphQLSchema schema() {
        SchemaParser schemaParser = SchemaParser.newParser()
                .file("graphql/error-handler-test.graphql")
                .resolvers(queryResolver())
                .build();
        return schemaParser.makeExecutableSchema();
      }
    }

  }
}
