package com.oembedler.moon.graphql.boot.test.graphqlJavaTools;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.coxautodev.graphql.tools.SchemaParserDictionary;
import com.oembedler.moon.graphql.boot.GraphQLJavaToolsAutoConfiguration;
import com.oembedler.moon.graphql.boot.ListSchemaStringProvider;
import com.oembedler.moon.graphql.boot.SchemaStringProvider;
import com.oembedler.moon.graphql.boot.test.AbstractAutoConfigurationTest;
import graphql.schema.GraphQLSchema;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
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

    @Configuration
    static class InterfaceConfiguration {
        class Query implements GraphQLQueryResolver {
            Interface theInterface() {
                return new Implementation();
            }
            Implementation theImplementation() {
                return new Implementation();
            }
        }
        interface Interface {
            String method();
        }
        class Implementation implements Interface {
            public String method() {
                return "method";
            }
        }

        @Bean
        public SchemaStringProvider schemaStringProvider() {
            ListSchemaStringProvider schemaStringProvider = new ListSchemaStringProvider();
            schemaStringProvider.add("type Query {"
                + "  theInterface:Interface!"
                + "} "
                + "interface Interface {"
                + "  method:String!"
                + "}"
                + "type Implementation implements Interface {"
                + "  method:String!"
                + "}");
            return schemaStringProvider;
        }

        @Bean
        public Query query() {
            return new Query();
        }

        @Bean
        public SchemaParserDictionary schemaParserDictionary() {
            return new SchemaParserDictionary().add(Implementation.class);
        }
    }

    @Test
    public void schemaWithInterfaceLoads() {
        load(InterfaceConfiguration.class);

        Assert.assertNotNull(this.getContext().getBean(GraphQLSchema.class));
    }
}
