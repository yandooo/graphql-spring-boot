package com.oembedler.moon.graphql.boot.test.springGraphqlCommon;

import com.oembedler.moon.graphql.GraphQLSchemaBeanFactory;
import com.oembedler.moon.graphql.boot.SpringGraphQLCommonAutoConfiguration;
import com.oembedler.moon.graphql.boot.test.AbstractAutoConfigurationTest;
import com.oembedler.moon.graphql.engine.GraphQLSchemaBuilder;
import com.oembedler.moon.graphql.engine.GraphQLSchemaConfig;
import graphql.schema.GraphQLSchema;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
@Ignore
public class SpringGraphQLCommonAutoConfigurationTest extends AbstractAutoConfigurationTest {

    public SpringGraphQLCommonAutoConfigurationTest() {
        super(SpringGraphQLCommonAutoConfiguration.class);
    }

    @Configuration
    @ComponentScan("com.oembedler.moon.graphql.boot.test.springGraphqlCommon")
    static class EmptyConfiguration {
    }

    @Test
    public void appContextLoads() {
        load(EmptyConfiguration.class);
        GraphQLSchemaBeanFactory graphQLSchemaBeanFactory = this.getContext().getBean(GraphQLSchemaBeanFactory.class);
        GraphQLSchemaConfig graphQLSchemaConfig = this.getContext().getBean(GraphQLSchemaConfig.class);
        GraphQLSchemaBuilder graphQLSchemaBuilder = this.getContext().getBean(GraphQLSchemaBuilder.class);

        Assert.assertNotNull(this.getContext().getBean(GraphQLSchema.class));
        Assert.assertTrue(graphQLSchemaBeanFactory.containsBean(GraphQLSchemaBeanFactory.class));
        Assert.assertEquals(graphQLSchemaBeanFactory, graphQLSchemaBeanFactory.getBeanByType(GraphQLSchemaBeanFactory.class));
        Assert.assertEquals(graphQLSchemaBeanFactory, graphQLSchemaBuilder.getGraphQLSchemaBeanFactory());
        Assert.assertEquals(graphQLSchemaConfig, graphQLSchemaBuilder.getGraphQLSchemaConfig());
    }
}
