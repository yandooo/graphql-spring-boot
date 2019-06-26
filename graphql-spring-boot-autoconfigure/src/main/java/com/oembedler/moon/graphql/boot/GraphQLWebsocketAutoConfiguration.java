package com.oembedler.moon.graphql.boot;

import graphql.servlet.GraphQLWebsocketServlet;
import graphql.servlet.core.ApolloSubscriptionConnectionListener;
import graphql.servlet.core.GraphQLObjectMapper;
import graphql.servlet.core.GraphQLQueryInvoker;
import graphql.servlet.core.SubscriptionConnectionListener;
import graphql.servlet.input.GraphQLInvocationInputFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServerEndpointRegistration;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import javax.websocket.server.ServerContainer;
import java.time.Duration;

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
    public SubscriptionConnectionListener subscriptionConnectionListener() {
        if (!apolloProperties.isKeepAliveEnabled()) {
            return ApolloSubscriptionConnectionListener.createWithKeepAliveDisabled();
        }
        return ApolloSubscriptionConnectionListener.createWithKeepAliveInterval(Duration.ofSeconds(apolloProperties.getKeepAliveIntervalSeconds()));
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLWebsocketServlet graphQLWebsocketServlet(GraphQLInvocationInputFactory invocationInputFactory, GraphQLQueryInvoker queryInvoker, GraphQLObjectMapper graphQLObjectMapper, SubscriptionConnectionListener subscriptionConnectionListener) {
        return new GraphQLWebsocketServlet(queryInvoker, invocationInputFactory, graphQLObjectMapper, subscriptionConnectionListener);
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
