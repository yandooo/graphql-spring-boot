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

import static graphql.kickstart.execution.GraphQLObjectMapper.newBuilder;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oembedler.moon.graphql.boot.metrics.MetricsInstrumentation;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.ExecutionStrategy;
import graphql.execution.SubscriptionExecutionStrategy;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.preparsed.PreparsedDocumentProvider;
import graphql.kickstart.execution.GraphQLObjectMapper;
import graphql.kickstart.execution.GraphQLQueryInvoker;
import graphql.kickstart.execution.config.DefaultExecutionStrategyProvider;
import graphql.kickstart.execution.config.ExecutionStrategyProvider;
import graphql.kickstart.execution.config.ObjectMapperConfigurer;
import graphql.kickstart.execution.config.ObjectMapperProvider;
import graphql.kickstart.execution.error.GraphQLErrorHandler;
import graphql.kickstart.spring.error.ErrorHandlerSupplier;
import graphql.kickstart.spring.error.GraphQLErrorStartupListener;
import graphql.kickstart.tools.boot.GraphQLJavaToolsAutoConfiguration;
import graphql.schema.GraphQLSchema;
import graphql.servlet.AbstractGraphQLHttpServlet;
import graphql.servlet.GraphQLConfiguration;
import graphql.servlet.GraphQLHttpServlet;
import graphql.servlet.config.DefaultGraphQLSchemaServletProvider;
import graphql.servlet.config.GraphQLSchemaServletProvider;
import graphql.servlet.context.GraphQLServletContextBuilder;
import graphql.servlet.core.GraphQLServletListener;
import graphql.servlet.core.GraphQLServletRootObjectBuilder;
import graphql.servlet.input.BatchInputPreProcessor;
import graphql.servlet.input.GraphQLInvocationInputFactory;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.servlet.MultipartConfigElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.DispatcherServlet;


/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
@Slf4j
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(DispatcherServlet.class)
@Conditional(OnSchemaOrSchemaProviderBean.class)
@ConditionalOnProperty(value = "graphql.servlet.enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter({GraphQLJavaToolsAutoConfiguration.class, JacksonAutoConfiguration.class})
@EnableConfigurationProperties({GraphQLServletProperties.class})
public class GraphQLWebAutoConfiguration {


  public static final String QUERY_EXECUTION_STRATEGY = "queryExecutionStrategy";
  public static final String MUTATION_EXECUTION_STRATEGY = "mutationExecutionStrategy";
  public static final String SUBSCRIPTION_EXECUTION_STRATEGY = "subscriptionExecutionStrategy";

  @Autowired
  private GraphQLServletProperties graphQLServletProperties;

  @Autowired(required = false)
  private List<GraphQLServletListener> listeners;

  @Autowired(required = false)
  private List<Instrumentation> instrumentations;

  @Autowired(required = false)
  private GraphQLErrorHandler errorHandler;

  private ErrorHandlerSupplier errorHandlerSupplier = new ErrorHandlerSupplier(null);

  @Autowired(required = false)
  private Map<String, ExecutionStrategy> executionStrategies;

  @Autowired(required = false)
  private GraphQLServletContextBuilder contextBuilder;

  @Autowired(required = false)
  private GraphQLServletRootObjectBuilder graphQLRootObjectBuilder;

  @Autowired(required = false)
  private ObjectMapperConfigurer objectMapperConfigurer;

  @Autowired(required = false)
  private PreparsedDocumentProvider preparsedDocumentProvider;

  @Autowired(required = false)
  private MultipartConfigElement multipartConfigElement;

  @Autowired(required = false)
  private BatchInputPreProcessor batchInputPreProcessor;

  @PostConstruct
  void postConstruct() {
    if (errorHandler != null) {
      errorHandlerSupplier.setErrorHandler(errorHandler);
    }
  }

  @Bean
  public GraphQLErrorStartupListener graphQLErrorStartupListener() {
    return new GraphQLErrorStartupListener(errorHandlerSupplier, graphQLServletProperties.isExceptionHandlersEnabled());
  }

  @Bean
  @ConditionalOnClass(CorsFilter.class)
  @ConditionalOnProperty(value = "graphql.servlet.corsEnabled", havingValue = "true", matchIfMissing = true)
  public CorsFilter corsConfigurer() {
    Map<String, CorsConfiguration> corsConfigurations = new LinkedHashMap<>(1);
    CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
    corsConfigurations.put(graphQLServletProperties.getCorsMapping(), corsConfiguration);

    UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
    configurationSource.setCorsConfigurations(corsConfigurations);
    configurationSource.setAlwaysUseFullPath(true);

    return new CorsFilter(configurationSource);
  }

  @Bean
  @ConditionalOnMissingBean
  public GraphQLSchemaServletProvider graphQLSchemaProvider(GraphQLSchema schema) {
    return new DefaultGraphQLSchemaServletProvider(schema);
  }

  @Bean
  @ConditionalOnMissingBean
  public ExecutionStrategyProvider executionStrategyProvider() {
    if (executionStrategies == null || executionStrategies.isEmpty()) {
      return new DefaultExecutionStrategyProvider(new AsyncExecutionStrategy(), null,
          new SubscriptionExecutionStrategy());
    } else if (executionStrategies.entrySet().size() == 1) {
      return new DefaultExecutionStrategyProvider(executionStrategies.entrySet().stream().findFirst().get().getValue());
    } else {

      if (!executionStrategies.containsKey(QUERY_EXECUTION_STRATEGY)) {
        throwIncorrectExecutionStrategyNameException();
      }

      if (executionStrategies.size() == 2 && !(executionStrategies.containsKey(MUTATION_EXECUTION_STRATEGY)
          || executionStrategies.containsKey(SUBSCRIPTION_EXECUTION_STRATEGY))) {
        throwIncorrectExecutionStrategyNameException();
      }

      if (executionStrategies.size() >= 3 && !(executionStrategies.containsKey(MUTATION_EXECUTION_STRATEGY)
          && executionStrategies.containsKey(SUBSCRIPTION_EXECUTION_STRATEGY))) {
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
    throw new IllegalStateException(String
        .format("When defining more than one execution strategy, they must be named %s, %s, or %s",
            QUERY_EXECUTION_STRATEGY, MUTATION_EXECUTION_STRATEGY, SUBSCRIPTION_EXECUTION_STRATEGY));
  }

  @Bean
  @ConditionalOnMissingBean
  public GraphQLInvocationInputFactory invocationInputFactory(GraphQLSchemaServletProvider schemaProvider) {
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
  public GraphQLQueryInvoker queryInvoker(
      ExecutionStrategyProvider executionStrategyProvider
  ) {
    GraphQLQueryInvoker.Builder builder = GraphQLQueryInvoker.newBuilder()
        .withExecutionStrategyProvider(executionStrategyProvider);

    if (instrumentations != null) {
      // Metrics instrumentation should be the last to run (we need that from TracingInstrumentation)
      instrumentations.sort((a, b) -> a instanceof MetricsInstrumentation ? 1 : 0);
      builder.with(instrumentations);
    }

    if (preparsedDocumentProvider != null) {
      builder.withPreparsedDocumentProvider(preparsedDocumentProvider);
    }

    return builder.build();
  }

  @Bean
  @ConditionalOnMissingBean
  public GraphQLObjectMapper graphQLObjectMapper(
      ObjectProvider<ObjectMapperProvider> objectMapperProviderObjectProvider) {
    GraphQLObjectMapper.Builder builder = newBuilder();

    builder.withGraphQLErrorHandler(errorHandlerSupplier);

    ObjectMapperProvider objectMapperProvider = objectMapperProviderObjectProvider.getIfAvailable();

    if (objectMapperProvider != null) {
      builder.withObjectMapperProvider(objectMapperProvider);
    } else if (objectMapperConfigurer != null) {
      builder.withObjectMapperConfigurer(objectMapperConfigurer);
    }
    log.info("Building GraphQLObjectMapper including errorHandler: {}", errorHandler);
    return builder.build();
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(value = "graphql.servlet.use-default-objectmapper", havingValue = "true",
      matchIfMissing = true)
  public ObjectMapperProvider objectMapperProvider(ObjectMapper objectMapper) {
    InjectableValues.Std injectableValues = new InjectableValues.Std();
    injectableValues.addValue(ObjectMapper.class, objectMapper);
    objectMapper.setInjectableValues(injectableValues);
    return () -> objectMapper;
  }

  @Bean
  @ConditionalOnMissingBean
  public GraphQLConfiguration graphQLServletConfiguration(GraphQLInvocationInputFactory invocationInputFactory,
      GraphQLQueryInvoker queryInvoker, GraphQLObjectMapper graphQLObjectMapper) {
    return GraphQLConfiguration.with(invocationInputFactory)
        .with(queryInvoker)
        .with(graphQLObjectMapper)
        .with(listeners)
        .with(graphQLServletProperties.isAsyncModeEnabled())
        .with(graphQLServletProperties.getSubscriptionTimeout())
        .with(batchInputPreProcessor)
        .with(graphQLServletProperties.getContextSetting())
        .build();
  }

  @Bean
  @ConditionalOnMissingBean
  public GraphQLHttpServlet graphQLHttpServlet(GraphQLConfiguration graphQLConfiguration) {
    return GraphQLHttpServlet.with(graphQLConfiguration);
  }

  @Bean
  public ServletRegistrationBean<AbstractGraphQLHttpServlet> graphQLServletRegistrationBean(
      AbstractGraphQLHttpServlet servlet) {
    ServletRegistrationBean<AbstractGraphQLHttpServlet> registration = new ServletRegistrationBean<>(servlet,
        graphQLServletProperties.getServletMapping());
    registration.setMultipartConfig(multipartConfigElement());
    return registration;
  }

  private MultipartConfigElement multipartConfigElement() {
    return Optional.ofNullable(multipartConfigElement).orElse(new MultipartConfigElement(""));
  }
}
