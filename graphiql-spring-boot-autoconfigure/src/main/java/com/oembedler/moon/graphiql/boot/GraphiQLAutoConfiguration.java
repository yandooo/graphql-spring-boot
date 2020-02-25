package com.oembedler.moon.graphiql.boot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;
import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;

/**
 * @author Andrew Potter
 * @author Ronny Bräunlich
 */
@Configuration
@ConditionalOnProperty(value = "graphiql.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(GraphiQLProperties.class)
public class GraphiQLAutoConfiguration {

    @Bean(name = "graphiQLController")
    @ConditionalOnWebApplication(type=SERVLET)
    ServletGraphiQLController servletGraphiQLController() {
        return new ServletGraphiQLController();
    }

    @Bean(name = "graphiQLController")
    @ConditionalOnMissingBean(ServletGraphiQLController.class)
    @ConditionalOnWebApplication(type=REACTIVE)
    ReactiveGraphiQLController reactiveGraphiQLController() {
        return new ReactiveGraphiQLController();
    }
}
