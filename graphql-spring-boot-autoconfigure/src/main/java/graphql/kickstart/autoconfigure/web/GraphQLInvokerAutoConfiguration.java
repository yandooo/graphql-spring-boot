package graphql.kickstart.autoconfigure.web;

import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentationOptions;
import graphql.kickstart.execution.BatchedDataLoaderGraphQLBuilder;
import graphql.kickstart.execution.GraphQLInvoker;
import graphql.kickstart.execution.config.GraphQLBuilder;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLInvokerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public GraphQLInvoker graphQLInvoker(
      GraphQLBuilder graphQLBuilder,
      BatchedDataLoaderGraphQLBuilder batchedDataLoaderGraphQLBuilder) {
    return new GraphQLInvoker(graphQLBuilder, batchedDataLoaderGraphQLBuilder);
  }

  @Bean
  @ConditionalOnMissingBean
  public BatchedDataLoaderGraphQLBuilder batchedDataLoaderGraphQLBuilder(
      @Autowired(required = false)
          Supplier<DataLoaderDispatcherInstrumentationOptions> optionsSupplier) {
    return new BatchedDataLoaderGraphQLBuilder(optionsSupplier);
  }
}
