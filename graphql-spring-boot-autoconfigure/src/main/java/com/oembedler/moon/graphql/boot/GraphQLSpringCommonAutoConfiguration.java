/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 oEmbedler Inc. and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 *  rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.oembedler.moon.graphql.boot;

import com.oembedler.moon.graphql.GraphQLSchemaBeanFactory;
import com.oembedler.moon.graphql.SpringGraphQLSchemaBeanFactory;
import com.oembedler.moon.graphql.engine.GraphQLSchemaBuilder;
import com.oembedler.moon.graphql.engine.GraphQLSchemaConfig;
import com.oembedler.moon.graphql.engine.stereotype.GraphQLSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
@Configuration
@EnableConfigurationProperties(GraphQLSpringCommonProperties.class)
public class GraphQLSpringCommonAutoConfiguration {

    @Autowired
    private GraphQLSpringCommonProperties graphQLSpringCommonProperties;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean
    public GraphQLSchemaBeanFactory graphQLSchemaBeanFactory() {
        return new SpringGraphQLSchemaBeanFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLSchemaConfig graphQLSchemaConfig() {
        GraphQLSchemaConfig graphQLSchemaConfig = new GraphQLSchemaConfig();

        // --- populate graphQLSpringCommonProperties config based on boot GraphQL properties
        if (graphQLSpringCommonProperties.getAllowEmptyClientMutationId() != null)
            graphQLSchemaConfig.setAllowEmptyClientMutationId(graphQLSpringCommonProperties.getAllowEmptyClientMutationId());
        if (graphQLSpringCommonProperties.getInjectClientMutationId() != null)
            graphQLSchemaConfig.setInjectClientMutationId(graphQLSpringCommonProperties.getInjectClientMutationId());
        if (StringUtils.hasText(graphQLSpringCommonProperties.getClientMutationIdName()))
            graphQLSchemaConfig.setClientMutationIdName(graphQLSpringCommonProperties.getClientMutationIdName());
        if (StringUtils.hasText(graphQLSpringCommonProperties.getInputObjectNamePrefix()))
            graphQLSchemaConfig.setInputObjectNamePrefix(graphQLSpringCommonProperties.getInputObjectNamePrefix());
        if (StringUtils.hasText(graphQLSpringCommonProperties.getMutationInputArgumentName()))
            graphQLSchemaConfig.setMutationInputArgumentName(graphQLSpringCommonProperties.getMutationInputArgumentName());
        if (StringUtils.hasText(graphQLSpringCommonProperties.getOutputObjectNamePrefix()))
            graphQLSchemaConfig.setOutputObjectNamePrefix(graphQLSpringCommonProperties.getOutputObjectNamePrefix());
        if (StringUtils.hasText(graphQLSpringCommonProperties.getSchemaMutationObjectName()))
            graphQLSchemaConfig.setSchemaMutationObjectName(graphQLSpringCommonProperties.getSchemaMutationObjectName());

        return graphQLSchemaConfig;
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLSchemaBuilder graphQLSchemaBuilder() {
        return new GraphQLSchemaBuilder(graphQLSchemaConfig(), graphQLSchemaBeanFactory());
    }

    @Bean
    @ConditionalOnMissingBean
    public graphql.schema.GraphQLSchema graphQLSchemaLocator() throws ClassNotFoundException {
        Set<Class<?>> schemaClasses = findSchemaClasses();

        if (schemaClasses.size() < 1) {
            throw new IllegalStateException("Could not create GraphQLSchema: No valid beans with @GraphQLSchema annotation found!");
        }

        if(schemaClasses.size() > 1) {
            throw new IllegalStateException("Could not create GraphQLSchema: More than one bean with @GraphQLSchema annotation found!  This auto-configuration does not support exposing multiple schemas, please create a graphql.java.GraphQLSchema bean manually or exclude this class from auto-configuration.");
        }

        return graphQLSchemaBuilder().buildSchema(schemaClasses.stream().findFirst().orElse(null)).getGraphQLSchema();
    }

    protected Set<Class<?>> findSchemaClasses() {
        // scans the application context for classes annotated with {@link GraphQLSchema}
        Map<String, Object> potentialCandidates = applicationContext.getBeansWithAnnotation(GraphQLSchema.class);
        return potentialCandidates.values().stream().map(x -> x.getClass()).collect(Collectors.toSet());
    }
}
