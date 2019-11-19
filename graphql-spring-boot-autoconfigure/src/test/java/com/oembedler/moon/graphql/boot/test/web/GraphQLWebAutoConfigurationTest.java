package com.oembedler.moon.graphql.boot.test.web;

import com.oembedler.moon.graphql.boot.GraphQLWebAutoConfiguration;
import com.oembedler.moon.graphql.boot.TransactionalGraphQLQueryInvokerWrapper;
import com.oembedler.moon.graphql.boot.test.AbstractAutoConfigurationTest;
import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.ExecutionStrategy;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.servlet.AbstractGraphQLHttpServlet;
import graphql.servlet.config.DefaultGraphQLSchemaProvider;
import graphql.servlet.config.GraphQLSchemaProvider;
import graphql.servlet.core.GraphQLQueryInvoker;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
public class GraphQLWebAutoConfigurationTest extends AbstractAutoConfigurationTest {

    public GraphQLWebAutoConfigurationTest() {
        super(AnnotationConfigWebApplicationContext.class, GraphQLWebAutoConfiguration.class);
    }

    private static final GraphQLSchema SCHEMA = GraphQLSchema.newSchema().query(GraphQLObjectType.newObject().name("Query").build()).build();

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

    @Test
    public void appContextLoadsWithNoExecutionStrategy() {
        load(SimpleConfiguration.class);

        Assert.assertNotNull(this.getContext().getBean(AbstractGraphQLHttpServlet.class));
    }

    @Test
    public void appContextLoadsWithOneExecutionStrategy() {
        load(OneExecutionStrategy.class);

        Assert.assertNotNull(this.getContext().getBean(AbstractGraphQLHttpServlet.class));
    }

    @Test
    public void appContextLoadsWithTwoExecutionStrategies() {
        load(TwoExecutionStrategies.class);

        Assert.assertNotNull(this.getContext().getBean(AbstractGraphQLHttpServlet.class));
    }

    @Test
    public void appContextLoadsWithThreeExecutionStrategies() {
        load(ThreeExecutionStrategies.class);

        Assert.assertNotNull(this.getContext().getBean(AbstractGraphQLHttpServlet.class));
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

    @Test
    public void appContextLoadsWithNoInstrumentation() {
        load(SimpleConfiguration.class);

        Assert.assertNotNull(this.getContext().getBean(AbstractGraphQLHttpServlet.class));
    }

    @Test
    public void appContextLoadsWithOneInstrumentation() {
        load(OneInstrumentationConfiguration.class);

        Assert.assertNotNull(this.getContext().getBean(AbstractGraphQLHttpServlet.class));
    }

    @Test
    public void appContextLoadsWithMultipleInstrumentations() {
        load(MultipleInstrumentationsConfiguration.class);

        Assert.assertNotNull(this.getContext().getBean(AbstractGraphQLHttpServlet.class));
    }

    @Configuration
    static class SchemaProviderConfiguration {
        @Bean
        GraphQLSchemaProvider schemaProvider() {
            return new DefaultGraphQLSchemaProvider(SCHEMA);
        }
    }

    @Test
    public void appContextLoadsWithCustomSchemaProvider() {
        load(SchemaProviderConfiguration.class);

        Assert.assertNotNull(this.getContext().getBean(AbstractGraphQLHttpServlet.class));
    }

    @Test
    public void queryInvokerShouldNotBeTransactionalByDefault() {
        load(SimpleConfiguration.class);
        assertThatQueryInvokerIsNotTransactional();
    }

    @Test
    public void queryInvokerShouldNotBeTransactionalIfDisabled() {
        load(SimpleConfiguration.class, "graphql.query-invoker.transactional=false");
        assertThatQueryInvokerIsNotTransactional();
    }

    @Test
    public void queryInvokerShouldBeTransactionalIfConfigured() {
        load(SimpleConfiguration.class, "graphql.query-invoker.transactional=true");
        assertThat(this.getContext().getBean(GraphQLQueryInvoker.class))
                .as("Should be a transactional query invoker.")
                .isInstanceOf(TransactionalGraphQLQueryInvokerWrapper.class);
    }

    private void assertThatQueryInvokerIsNotTransactional() {
        assertThat(this.getContext().getBean(GraphQLQueryInvoker.class))
                .as("Should be a non-transactional query invoker.")
                .isNotNull().isNotInstanceOf(TransactionalGraphQLQueryInvokerWrapper.class);
    }
}
