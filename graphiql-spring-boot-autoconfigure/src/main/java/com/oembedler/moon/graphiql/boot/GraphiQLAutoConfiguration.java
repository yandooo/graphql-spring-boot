package com.oembedler.moon.graphiql.boot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author Andrew Potter
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(DispatcherServlet.class)
@EnableConfigurationProperties(GraphiQLProperties.class)
public class GraphiQLAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "graphiql.enabled", havingValue = "true", matchIfMissing = true)
    GraphiQLController graphiQLController() {
        return new GraphiQLController();
    }

}
