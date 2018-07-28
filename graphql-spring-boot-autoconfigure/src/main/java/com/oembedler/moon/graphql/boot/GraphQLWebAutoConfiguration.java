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

import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.ExecutionStrategy;
import graphql.execution.SubscriptionExecutionStrategy;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.preparsed.PreparsedDocumentProvider;
import graphql.schema.GraphQLSchema;
import graphql.servlet.AbstractGraphQLHttpServlet;
import graphql.servlet.DefaultExecutionStrategyProvider;
import graphql.servlet.DefaultGraphQLSchemaProvider;
import graphql.servlet.ExecutionStrategyProvider;
import graphql.servlet.GraphQLContextBuilder;
import graphql.servlet.GraphQLErrorHandler;
import graphql.servlet.GraphQLInvocationInputFactory;
import graphql.servlet.GraphQLObjectMapper;
import graphql.servlet.GraphQLQueryInvoker;
import graphql.servlet.GraphQLRootObjectBuilder;
import graphql.servlet.GraphQLSchemaProvider;
import graphql.servlet.GraphQLServletListener;
import graphql.servlet.GraphQLWebsocketServlet;
import graphql.servlet.ObjectMapperConfigurer;
import graphql.servlet.SimpleGraphQLHttpServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.CorsRegistryWorkaround;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServerEndpointRegistration;

import javax.servlet.MultipartConfigElement;
import java.util.List;
import java.util.Map;


/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(DispatcherServlet.class)
@ConditionalOnBean({GraphQLSchema.class})
@ConditionalOnProperty(value = "graphql.servlet.enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter({GraphQLJavaToolsAutoConfiguration.class})
@EnableConfigurationProperties({GraphQLServletProperties.class})
public class GraphQLWebAutoConfiguration {

    public static final String QUERY_EXECUTION_STRATEGY = "queryExecutionStrategy";
    public static final String MUTATION_EXECUTION_STRATEGY = "mutationExecutionStrategy";
    public static final String SUBSCRIPTION_EXECUTION_STRATEGY = "subscriptionExecutionStrategy";

    @Autowired
    private GraphQLServletProperties graphQLServletProperties;

    @Value("${graphql.servlet.subscriptions.websocket.path:/subscriptions}")
    private String websocketPath;

    @Autowired(required = false)
    private List<GraphQLServletListener> listeners;

    @Autowired(required = false)
    private Instrumentation instrumentation;

    @Autowired(required = false)
    private GraphQLErrorHandler errorHandler;

    @Autowired(required = false)
    private Map<String, ExecutionStrategy> executionStrategies;

    @Autowired(required = false)
    private GraphQLContextBuilder contextBuilder;

    @Autowired(required = false)
    private GraphQLRootObjectBuilder graphQLRootObjectBuilder;

    @Autowired(required = false)
    private ObjectMapperConfigurer objectMapperConfigurer;

    @Autowired(required = false)
    private PreparsedDocumentProvider preparsedDocumentProvider;

    @Bean
    @ConditionalOnClass(CorsFilter.class)
    @ConditionalOnProperty(value = "graphql.servlet.corsEnabled", havingValue = "true", matchIfMissing = true)
    public CorsFilter corsConfigurer() {
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.setCorsConfigurations(CorsRegistryWorkaround.getCorsConfiguration(graphQLServletProperties.getCorsMapping()));
        configurationSource.setAlwaysUseFullPath(true);

        return new CorsFilter(configurationSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLSchemaProvider graphQLSchemaProvider(GraphQLSchema schema) {
        return new DefaultGraphQLSchemaProvider(schema);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExecutionStrategyProvider executionStrategyProvider() {
        if (executionStrategies == null || executionStrategies.isEmpty()) {
            return new DefaultExecutionStrategyProvider(new AsyncExecutionStrategy(), null, new SubscriptionExecutionStrategy());
        } else if (executionStrategies.entrySet().size() == 1) {
            return new DefaultExecutionStrategyProvider(executionStrategies.entrySet().stream().findFirst().get().getValue());
        } else {

            if (!executionStrategies.containsKey(QUERY_EXECUTION_STRATEGY)) {
                throwIncorrectExecutionStrategyNameException();
            }

            if (executionStrategies.size() == 2 && !(executionStrategies.containsKey(MUTATION_EXECUTION_STRATEGY) || executionStrategies.containsKey(SUBSCRIPTION_EXECUTION_STRATEGY))) {
                throwIncorrectExecutionStrategyNameException();
            }

            if (executionStrategies.size() >= 3 && !(executionStrategies.containsKey(MUTATION_EXECUTION_STRATEGY) && executionStrategies.containsKey(SUBSCRIPTION_EXECUTION_STRATEGY))) {
                throwIncorrectExecutionStrategyNameException();
            }

            return new DefaultExecutionStrategyProvider(
                    executionStrategies.get(QUERY_EXECUTION_STRATEGY),
                    executionStrategies.get(MUTATION_EXECUTION_STRATEGY),
                    executionStrategies.get(SUBSCRIPTION_EXECUTION_STRATEGY)
            );
        }
    }

    private void throwIncorrectExecutionStrategyNameException() {
        throw new IllegalStateException(String.format("When defining more than one execution strategy, they must be named %s, %s, or %s", QUERY_EXECUTION_STRATEGY, MUTATION_EXECUTION_STRATEGY, SUBSCRIPTION_EXECUTION_STRATEGY));
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLInvocationInputFactory invocationInputFactory(GraphQLSchemaProvider schemaProvider) {
        GraphQLInvocationInputFactory.Builder builder = GraphQLInvocationInputFactory.newBuilder(schemaProvider);

        if (graphQLRootObjectBuilder != null) {
            builder.withGraphQLRootObjectBuilder(graphQLRootObjectBuilder);
        }

        if (contextBuilder != null) {
            builder.withGraphQLContextBuilder(contextBuilder);
        }

        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLQueryInvoker queryInvoker(ExecutionStrategyProvider executionStrategyProvider) {
        GraphQLQueryInvoker.Builder builder = GraphQLQueryInvoker.newBuilder()
                .withExecutionStrategyProvider(executionStrategyProvider);

        if (instrumentation != null) {
            builder.withInstrumentation(instrumentation);
        }

        if (preparsedDocumentProvider != null) {
            builder.withPreparsedDocumentProvider(preparsedDocumentProvider);
        }

        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLObjectMapper graphQLObjectMapper() {
        GraphQLObjectMapper.Builder builder = GraphQLObjectMapper.newBuilder();

        if (errorHandler != null) {
            builder.withGraphQLErrorHandler(errorHandler);
        }

        if (objectMapperConfigurer != null) {
            builder.withObjectMapperConfigurer(objectMapperConfigurer);
        }

        return builder.build();
    }


    @Bean
    @ConditionalOnMissingBean
    public SimpleGraphQLHttpServlet graphQLHttpServlet(GraphQLInvocationInputFactory invocationInputFactory, GraphQLQueryInvoker queryInvoker, GraphQLObjectMapper graphQLObjectMapper) {
        return SimpleGraphQLHttpServlet.newBuilder(invocationInputFactory)
                .withQueryInvoker(queryInvoker)
                .withObjectMapper(graphQLObjectMapper)
                .build();
    }

    @Bean
    public ServletRegistrationBean<AbstractGraphQLHttpServlet> graphQLServletRegistrationBean(AbstractGraphQLHttpServlet servlet, MultipartConfigElement multipartConfig) {
        ServletRegistrationBean<AbstractGraphQLHttpServlet> registration = new ServletRegistrationBean<>(servlet, graphQLServletProperties.getServletMapping());
        registration.setMultipartConfig(multipartConfig);
        return registration;
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLWebsocketServlet graphQLWebsocketServlet(GraphQLInvocationInputFactory invocationInputFactory, GraphQLQueryInvoker queryInvoker, GraphQLObjectMapper graphQLObjectMapper) {
        return new GraphQLWebsocketServlet(queryInvoker, invocationInputFactory, graphQLObjectMapper);
    }

    @Bean
    public ServerEndpointRegistration serverEndpointRegistration(GraphQLWebsocketServlet servlet) {
        return new GraphQLWsServerEndpointRegistration(websocketPath, servlet);
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    @ConditionalOnMissingBean
    public MultipartConfigElement multipartConfigElement() {
        return new MultipartConfigElement("");
    }
}
