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
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@ConditionalOnClass(GraphQLSchemaConfig.class)
@EnableConfigurationProperties(SpringGraphQLCommonProperties.class)
public class SpringGraphQLCommonAutoConfiguration {

    @Autowired
    private SpringGraphQLCommonProperties springGraphQLCommonProperties;

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
        if (springGraphQLCommonProperties.getAllowEmptyClientMutationId() != null)
            graphQLSchemaConfig.setAllowEmptyClientMutationId(springGraphQLCommonProperties.getAllowEmptyClientMutationId());
        if (springGraphQLCommonProperties.getInjectClientMutationId() != null)
            graphQLSchemaConfig.setInjectClientMutationId(springGraphQLCommonProperties.getInjectClientMutationId());
        if (StringUtils.hasText(springGraphQLCommonProperties.getClientMutationIdName()))
            graphQLSchemaConfig.setClientMutationIdName(springGraphQLCommonProperties.getClientMutationIdName());
        if (StringUtils.hasText(springGraphQLCommonProperties.getInputObjectNamePrefix()))
            graphQLSchemaConfig.setInputObjectNamePrefix(springGraphQLCommonProperties.getInputObjectNamePrefix());
        if (StringUtils.hasText(springGraphQLCommonProperties.getMutationInputArgumentName()))
            graphQLSchemaConfig.setMutationInputArgumentName(springGraphQLCommonProperties.getMutationInputArgumentName());
        if (StringUtils.hasText(springGraphQLCommonProperties.getOutputObjectNamePrefix()))
            graphQLSchemaConfig.setOutputObjectNamePrefix(springGraphQLCommonProperties.getOutputObjectNamePrefix());
        if (StringUtils.hasText(springGraphQLCommonProperties.getSchemaMutationObjectName()))
            graphQLSchemaConfig.setSchemaMutationObjectName(springGraphQLCommonProperties.getSchemaMutationObjectName());

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
