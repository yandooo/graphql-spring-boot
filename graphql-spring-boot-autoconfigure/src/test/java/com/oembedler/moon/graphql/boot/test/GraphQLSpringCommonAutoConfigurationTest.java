package com.oembedler.moon.graphql.boot.test;

import com.oembedler.moon.graphql.GraphQLSchemaBeanFactory;
import com.oembedler.moon.graphql.boot.GraphQLSpringCommonAutoConfiguration;
import com.oembedler.moon.graphql.engine.GraphQLSchemaBuilder;
import com.oembedler.moon.graphql.engine.GraphQLSchemaConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
public class GraphQLSpringCommonAutoConfigurationTest {

    private static final String BASE_PACKAGE = "com.oembedler.moon.graphql.boot.test";
    private AnnotationConfigApplicationContext context;

    @After
    public void tearDown() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    public void defaultSettingsLoad() {
        load(EmptyConfiguration.class, "");
        GraphQLSchemaBeanFactory graphQLSchemaBeanFactory = this.context.getBean(GraphQLSchemaBeanFactory.class);
        GraphQLSchemaConfig graphQLSchemaConfig = this.context.getBean(GraphQLSchemaConfig.class);
        GraphQLSchemaBuilder graphQLSchemaBuilder = this.context.getBean(GraphQLSchemaBuilder.class);

        Assert.assertTrue(graphQLSchemaBeanFactory.containsBean(GraphQLSchemaBeanFactory.class));
        Assert.assertEquals(graphQLSchemaBeanFactory, graphQLSchemaBeanFactory.getBeanByType(GraphQLSchemaBeanFactory.class));
        Assert.assertEquals(graphQLSchemaBeanFactory, graphQLSchemaBuilder.getGraphQLSchemaBeanFactory());
        Assert.assertEquals(graphQLSchemaConfig, graphQLSchemaBuilder.getGraphQLSchemaConfig());
    }

    private void load(Class<?> config, String... environment) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        EnvironmentTestUtils.addEnvironment(applicationContext, environment);
        applicationContext.register(config);
        applicationContext.register(GraphQLSpringCommonAutoConfiguration.class);
        applicationContext.refresh();
        this.context = applicationContext;
    }

    @Configuration
    @ComponentScan(BASE_PACKAGE)
    static class EmptyConfiguration {
    }

}
