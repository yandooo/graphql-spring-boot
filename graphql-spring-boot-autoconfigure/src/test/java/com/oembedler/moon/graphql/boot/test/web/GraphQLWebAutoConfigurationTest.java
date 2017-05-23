package com.oembedler.moon.graphql.boot.test.web;

import com.oembedler.moon.graphql.boot.GraphQLWebAutoConfiguration;
import com.oembedler.moon.graphql.boot.test.AbstractAutoConfigurationTest;
import graphql.execution.ExecutionStrategy;
import graphql.execution.SimpleExecutionStrategy;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLServlet;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
public class GraphQLWebAutoConfigurationTest extends AbstractAutoConfigurationTest {

    public GraphQLWebAutoConfigurationTest() {
        super(AnnotationConfigWebApplicationContext.class, GraphQLWebAutoConfiguration.class);
    }

    @Configuration
    static class SimpleConfiguration {
        @Bean
        GraphQLSchema schema() {
            return GraphQLSchema.newSchema().query(GraphQLObjectType.newObject().name("Query").build()).build();
        }
    }

    @Configuration
    static class OneExecutionStrategy extends SimpleConfiguration {
        @Bean
        public ExecutionStrategy executionStrategy() {
            return new SimpleExecutionStrategy();
        }
    }

    @Configuration
    static class TwoExecutionStrategies extends SimpleConfiguration {
        @Bean
        public ExecutionStrategy queryExecutionStrategy() {
            return new SimpleExecutionStrategy();
        }

        @Bean
        public ExecutionStrategy mutationExecutionStrategy() {
            return new SimpleExecutionStrategy();
        }
    }

    @Configuration
    static class ThreeExecutionStrategies extends SimpleConfiguration {
        @Bean
        public ExecutionStrategy queryExecutionStrategy() {
            return new SimpleExecutionStrategy();
        }

        @Bean
        public ExecutionStrategy mutationExecutionStrategy() {
            return new SimpleExecutionStrategy();
        }

        @Bean
        public ExecutionStrategy subscriptionExecutionStrategy() {
            return new SimpleExecutionStrategy();
        }
    }

    @Test
    public void appContextLoadsWithNoExecutionStrategy() {
        load(SimpleConfiguration.class);

        Assert.assertNotNull(this.getContext().getBean(GraphQLServlet.class));
    }

    @Test
    public void appContextLoadsWithOneExecutionStrategy() { load(OneExecutionStrategy.class);

        Assert.assertNotNull(this.getContext().getBean(GraphQLServlet.class));
    }

    @Test
    public void appContextLoadsWithTwoExecutionStrategies() {
        load(TwoExecutionStrategies.class);

        Assert.assertNotNull(this.getContext().getBean(GraphQLServlet.class));
    }

    @Test
    public void appContextLoadsWithThreeExecutionStrategies() {
        load(ThreeExecutionStrategies.class);

        Assert.assertNotNull(this.getContext().getBean(GraphQLServlet.class));
    }
}
