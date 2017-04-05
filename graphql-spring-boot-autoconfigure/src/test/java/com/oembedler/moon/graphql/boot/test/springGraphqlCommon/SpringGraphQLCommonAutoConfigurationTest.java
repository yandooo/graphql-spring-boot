package com.oembedler.moon.graphql.boot.test.springGraphqlCommon;

import com.oembedler.moon.graphql.GraphQLSchemaBeanFactory;
import com.oembedler.moon.graphql.boot.SpringGraphQLCommonAutoConfiguration;
import com.oembedler.moon.graphql.boot.test.AutoConfigurationTest;
import com.oembedler.moon.graphql.engine.GraphQLSchemaBuilder;
import com.oembedler.moon.graphql.engine.GraphQLSchemaConfig;
import graphql.schema.GraphQLSchema;
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
public class SpringGraphQLCommonAutoConfigurationTest extends AutoConfigurationTest {

    protected SpringGraphQLCommonAutoConfigurationTest() {
        super(SpringGraphQLCommonAutoConfiguration.class);
    }

    @Configuration
    @ComponentScan("com.oembedler.moon.graphql.boot.test.springGraphqlCommon")
    static class EmptyConfiguration {
    }

    @Test
    public void appContextLoads() {
        load(EmptyConfiguration.class);
        GraphQLSchemaBeanFactory graphQLSchemaBeanFactory = this.context.getBean(GraphQLSchemaBeanFactory.class);
        GraphQLSchemaConfig graphQLSchemaConfig = this.context.getBean(GraphQLSchemaConfig.class);
        GraphQLSchemaBuilder graphQLSchemaBuilder = this.context.getBean(GraphQLSchemaBuilder.class);

        Assert.assertNotNull(this.context.getBean(GraphQLSchema.class));
        Assert.assertTrue(graphQLSchemaBeanFactory.containsBean(GraphQLSchemaBeanFactory.class));
        Assert.assertEquals(graphQLSchemaBeanFactory, graphQLSchemaBeanFactory.getBeanByType(GraphQLSchemaBeanFactory.class));
        Assert.assertEquals(graphQLSchemaBeanFactory, graphQLSchemaBuilder.getGraphQLSchemaBeanFactory());
        Assert.assertEquals(graphQLSchemaConfig, graphQLSchemaBuilder.getGraphQLSchemaConfig());
    }
}
