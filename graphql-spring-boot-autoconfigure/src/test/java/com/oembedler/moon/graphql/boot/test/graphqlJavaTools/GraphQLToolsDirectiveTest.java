package com.oembedler.moon.graphql.boot.test.graphqlJavaTools;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.oembedler.moon.graphql.boot.GraphQLJavaToolsAutoConfiguration;
import com.oembedler.moon.graphql.boot.SchemaDirective;
import com.oembedler.moon.graphql.boot.test.AbstractAutoConfigurationTest;
import graphql.schema.GraphQLObjectType;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

public class GraphQLToolsDirectiveTest extends AbstractAutoConfigurationTest {

    public GraphQLToolsDirectiveTest() {
        super(GraphQLJavaToolsAutoConfiguration.class);
    }

    @Test
    public void directiveIsLoaded() {
        System.setProperty("graphql.tools.schemaLocationPattern", "graphql/schema-directive-test.graphql");
        load(BaseConfiguration.class);
    }

    @Configuration
    static class BaseConfiguration {

        @Component
        public class Query implements GraphQLQueryResolver {
            String schemaLocationTest(String id) {
                return id;
            }
        }

        @Bean
        public SchemaDirective uppercaseDirective() {
            return new SchemaDirective("uppercase", new SchemaDirectiveWiring() {
                @Override
                public GraphQLObjectType onObject(SchemaDirectiveWiringEnvironment<GraphQLObjectType> environment) {
                    return null;
                }
            });
        }
    }
}
