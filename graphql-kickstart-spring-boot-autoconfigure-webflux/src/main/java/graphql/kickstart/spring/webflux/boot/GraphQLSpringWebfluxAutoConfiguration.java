package graphql.kickstart.spring.webflux.boot;

import static graphql.kickstart.execution.GraphQLObjectMapper.newBuilder;

import graphql.GraphQL;
import graphql.kickstart.execution.GraphQLInvoker;
import graphql.kickstart.execution.GraphQLObjectMapper;
import graphql.kickstart.execution.config.GraphQLBuilder;
import graphql.kickstart.execution.config.ObjectMapperProvider;
import graphql.kickstart.spring.DefaultGraphQLSpringInvocationInputFactory;
import graphql.kickstart.spring.GraphQLSpringContextBuilder;
import graphql.kickstart.spring.GraphQLSpringInvocationInputFactory;
import graphql.kickstart.spring.GraphQLSpringRootObjectBuilder;
import graphql.kickstart.spring.webflux.GraphQLController;
import graphql.schema.GraphQLSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan(basePackageClasses = GraphQLController.class)
public class GraphQLSpringWebfluxAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public GraphQLObjectMapper graphQLObjectMapper(ObjectProvider<ObjectMapperProvider> provider) {
    GraphQLObjectMapper.Builder builder = newBuilder();
//    builder.withGraphQLErrorHandler(errorHandlerSupplier);
    provider.ifAvailable(builder::withObjectMapperProvider);
    return builder.build();
  }

  @Bean
  @ConditionalOnMissingBean
  public GraphQLSpringInvocationInputFactory graphQLSpringInvocationInputFactory(
      @Autowired(required = false) GraphQLSpringContextBuilder contextBuilder,
      @Autowired(required = false) GraphQLSpringRootObjectBuilder rootObjectBuilder
  ) {
    return new DefaultGraphQLSpringInvocationInputFactory(contextBuilder, rootObjectBuilder);
  }

  @Bean
  @ConditionalOnMissingBean
  public GraphQLInvoker graphQLInvoker(GraphQLSchema schema) {
    GraphQL graphQL = new GraphQLBuilder().build(schema);
    return new GraphQLInvoker(graphQL);
  }

}
