package graphql.kickstart.spring.web.boot.test.web;

import graphql.kickstart.servlet.AbstractGraphQLHttpServlet;
import graphql.kickstart.servlet.config.DefaultGraphQLSchemaServletProvider;
import graphql.kickstart.servlet.config.GraphQLSchemaServletProvider;
import graphql.kickstart.spring.web.boot.GraphQLWebAutoConfiguration;
import graphql.kickstart.spring.web.boot.test.AbstractAutoConfigurationTest;
import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.ExecutionStrategy;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
public class GraphQLWebAutoConfigurationTest extends AbstractAutoConfigurationTest {

  private static final GraphQLSchema SCHEMA = GraphQLSchema.newSchema()
      .query(GraphQLObjectType.newObject().name("Query").build()).build();

  public GraphQLWebAutoConfigurationTest() {
    super(AnnotationConfigWebApplicationContext.class, GraphQLWebAutoConfiguration.class);
  }

  @Test
  public void appContextLoadsWithNoExecutionStrategy() {
    load(SimpleConfiguration.class);

    assertThat(this.getContext().getBean(AbstractGraphQLHttpServlet.class)).isNotNull();
  }

  @Test
  public void appContextLoadsWithOneExecutionStrategy() {
    load(OneExecutionStrategy.class);

    assertThat(this.getContext().getBean(AbstractGraphQLHttpServlet.class)).isNotNull();
  }

  @Test
  public void appContextLoadsWithTwoExecutionStrategies() {
    load(TwoExecutionStrategies.class);

    assertThat(this.getContext().getBean(AbstractGraphQLHttpServlet.class)).isNotNull();
  }

  @Test
  public void appContextLoadsWithThreeExecutionStrategies() {
    load(ThreeExecutionStrategies.class);

    assertThat(this.getContext().getBean(AbstractGraphQLHttpServlet.class)).isNotNull();
  }

  @Test
  public void appContextLoadsWithNoInstrumentation() {
    load(SimpleConfiguration.class);

    assertThat(this.getContext().getBean(AbstractGraphQLHttpServlet.class)).isNotNull();
  }

  @Test
  public void appContextLoadsWithOneInstrumentation() {
    load(OneInstrumentationConfiguration.class);

    assertThat(this.getContext().getBean(AbstractGraphQLHttpServlet.class)).isNotNull();
  }

  @Test
  public void appContextLoadsWithMultipleInstrumentations() {
    load(MultipleInstrumentationsConfiguration.class);

    assertThat(this.getContext().getBean(AbstractGraphQLHttpServlet.class)).isNotNull();
  }

  @Test
  public void appContextLoadsWithCustomSchemaProvider() {
    load(SchemaProviderConfiguration.class);

    assertThat(this.getContext().getBean(AbstractGraphQLHttpServlet.class)).isNotNull();
  }

  @Configuration
  static class SimpleConfiguration {

    @Bean
    GraphQLSchema schema() {
      return SCHEMA;
    }

  }

  @Configuration
  static class OneExecutionStrategy extends SimpleConfiguration {

    @Bean
    public ExecutionStrategy executionStrategy() {
      return new AsyncExecutionStrategy();
    }
  }

  @Configuration
  static class TwoExecutionStrategies extends SimpleConfiguration {

    @Bean
    public ExecutionStrategy queryExecutionStrategy() {
      return new AsyncExecutionStrategy();
    }

    @Bean
    public ExecutionStrategy mutationExecutionStrategy() {
      return new AsyncExecutionStrategy();
    }
  }

  @Configuration
  static class ThreeExecutionStrategies extends SimpleConfiguration {

    @Bean
    public ExecutionStrategy queryExecutionStrategy() {
      return new AsyncExecutionStrategy();
    }

    @Bean
    public ExecutionStrategy mutationExecutionStrategy() {
      return new AsyncExecutionStrategy();
    }

    @Bean
    public ExecutionStrategy subscriptionExecutionStrategy() {
      return new AsyncExecutionStrategy();
    }
  }

  @Configuration
  static class OneInstrumentationConfiguration extends SimpleConfiguration {

    @Bean
    public TracingInstrumentation tracingInstrumentation() {
      return new TracingInstrumentation();
    }
  }

  @Configuration
  static class MultipleInstrumentationsConfiguration extends OneInstrumentationConfiguration {

    @Bean
    public MaxQueryComplexityInstrumentation maxQueryComplexityInstrumentation() {
      return new MaxQueryComplexityInstrumentation(10);
    }

    @Bean
    public MaxQueryDepthInstrumentation maxQueryDepthInstrumentation() {
      return new MaxQueryDepthInstrumentation(10);
    }
  }

  @Configuration
  static class SchemaProviderConfiguration {

    @Bean
    GraphQLSchemaServletProvider schemaProvider() {
      return new DefaultGraphQLSchemaServletProvider(SCHEMA);
    }
  }

}
