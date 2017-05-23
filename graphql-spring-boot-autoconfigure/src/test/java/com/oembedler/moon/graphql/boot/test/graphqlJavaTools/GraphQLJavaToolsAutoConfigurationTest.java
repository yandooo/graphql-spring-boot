package com.oembedler.moon.graphql.boot.test.graphqlJavaTools;

import com.oembedler.moon.graphql.boot.GraphQLJavaToolsAutoConfiguration;
import com.oembedler.moon.graphql.boot.test.AbstractAutoConfigurationTest;
import graphql.schema.GraphQLSchema;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
public class GraphQLJavaToolsAutoConfigurationTest extends AbstractAutoConfigurationTest {

    public GraphQLJavaToolsAutoConfigurationTest() {
        super(GraphQLJavaToolsAutoConfiguration.class);
    }

    @Configuration
    @ComponentScan("com.oembedler.moon.graphql.boot.test.graphqlJavaTools")
    static class BaseConfiguration {
    }

    @Test
    public void appContextLoads() {
        load(BaseConfiguration.class);

        Assert.assertNotNull(this.getContext().getBean(GraphQLSchema.class));
    }
}
