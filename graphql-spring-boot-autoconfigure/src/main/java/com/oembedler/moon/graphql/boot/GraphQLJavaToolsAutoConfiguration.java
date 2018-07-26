package com.oembedler.moon.graphql.boot;

import com.coxautodev.graphql.tools.*;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLSchemaProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

/**
 * @author Andrew Potter
 */
@Configuration
@ConditionalOnClass(SchemaParser.class)
public class GraphQLJavaToolsAutoConfiguration {

    @Autowired(required = false)
    private SchemaParserDictionary dictionary;

    @Autowired(required = false)
    private GraphQLScalarType[] scalars;

    @Autowired(required = false)
    private SchemaParserOptions options;

    @Bean
    @ConditionalOnMissingBean
    public SchemaStringProvider schemaStringProvider() {
        return new ClasspathResourceSchemaStringProvider();
    }

    @Bean
    @ConditionalOnBean({GraphQLResolver.class})
    @ConditionalOnMissingBean
    public SchemaParser schemaParser(List<GraphQLResolver<?>> resolvers, SchemaStringProvider schemaStringProvider) throws IOException {
        SchemaParserBuilder builder = dictionary != null ? new SchemaParserBuilder(dictionary) : new SchemaParserBuilder();

        List<String> schemaStrings = schemaStringProvider.schemaStrings();
        schemaStrings.forEach(builder::schemaString);

        if (scalars != null) {
            builder.scalars(scalars);
        }

        if (options != null) {
            builder.options(options);
        }

        return builder.resolvers(resolvers)
                .build();
    }


    @Bean
    @ConditionalOnBean(SchemaParser.class)
    @ConditionalOnMissingBean({GraphQLSchema.class, GraphQLSchemaProvider.class})
    public GraphQLSchema graphQLSchema(SchemaParser schemaParser) {
        return schemaParser.makeExecutableSchema();
    }
}
