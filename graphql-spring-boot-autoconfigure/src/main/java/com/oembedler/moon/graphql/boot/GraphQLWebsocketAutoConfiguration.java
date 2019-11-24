package com.oembedler.moon.graphql.boot;

import graphql.GraphQL;
import graphql.kickstart.execution.GraphQLInvoker;
import graphql.kickstart.execution.GraphQLObjectMapper;
import graphql.kickstart.execution.config.GraphQLBuilder;
import graphql.kickstart.execution.subscriptions.GraphQLSubscriptionInvocationInputFactory;
import graphql.kickstart.execution.subscriptions.SubscriptionConnectionListener;
import graphql.kickstart.execution.subscriptions.apollo.KeepAliveSubscriptionConnectionListener;
import graphql.kickstart.tools.boot.GraphQLJavaToolsAutoConfiguration;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLWebsocketServlet;
import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.websocket.server.ServerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServerEndpointRegistration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(DispatcherServlet.class)
@Conditional(OnSchemaOrSchemaProviderBean.class)
@ConditionalOnProperty(value = "graphql.servlet.websocket.enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter({GraphQLJavaToolsAutoConfiguration.class})
@EnableConfigurationProperties(GraphQLSubscriptionApolloProperties.class)
public class GraphQLWebsocketAutoConfiguration {

  @Value("${graphql.servlet.subscriptions.websocket.path:/subscriptions}")
  private String websocketPath;

  @Autowired
  private GraphQLSubscriptionApolloProperties apolloProperties;

  @Bean
  @ConditionalOnMissingBean
  public GraphQLInvoker graphQLInvoker(GraphQLSchema schema) {
    GraphQL graphQL = new GraphQLBuilder().build(schema);
    return new GraphQLInvoker(graphQL);
  }

  @Bean
  @ConditionalOnMissingBean
  public GraphQLWebsocketServlet graphQLWebsocketServlet(
      GraphQLSubscriptionInvocationInputFactory invocationInputFactory,
      GraphQLInvoker graphQLInvoker,
      GraphQLObjectMapper graphQLObjectMapper,
      @Autowired(required = false) Collection<SubscriptionConnectionListener> connectionListeners) {
    Set<SubscriptionConnectionListener> listeners = new HashSet<>();
    if (connectionListeners != null) {
      listeners.addAll(connectionListeners);
    }
    keepAliveListener().ifPresent(listeners::add);
    return new GraphQLWebsocketServlet(graphQLInvoker, invocationInputFactory, graphQLObjectMapper, listeners);
  }

  private Optional<SubscriptionConnectionListener> keepAliveListener() {
    if (apolloProperties.isKeepAliveEnabled()) {
      return Optional.of(new KeepAliveSubscriptionConnectionListener(
          Duration.ofSeconds(apolloProperties.getKeepAliveIntervalSeconds()))
      );
    }
    return Optional.empty();
  }

  @Bean
  @ConditionalOnClass(ServerContainer.class)
  public ServerEndpointRegistration serverEndpointRegistration(GraphQLWebsocketServlet servlet) {
    return new GraphQLWsServerEndpointRegistration(websocketPath, servlet);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnClass(ServerContainer.class)
  public ServerEndpointExporter serverEndpointExporter() {
    return new ServerEndpointExporter();
  }

}
